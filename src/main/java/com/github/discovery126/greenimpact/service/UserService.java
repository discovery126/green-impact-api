package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.RegisterDto;
import com.github.discovery126.greenimpact.dto.request.UserRequest;
import com.github.discovery126.greenimpact.dto.request.UserUpdateRequest;
import com.github.discovery126.greenimpact.dto.response.RatingResponse;
import com.github.discovery126.greenimpact.dto.response.TaskResponse;
import com.github.discovery126.greenimpact.dto.response.UserResponse;
import com.github.discovery126.greenimpact.exception.UserNotFoundException;
import com.github.discovery126.greenimpact.exception.UsernameAlreadyExistsException;
import com.github.discovery126.greenimpact.mapper.UserMapper;
import com.github.discovery126.greenimpact.model.City;
import com.github.discovery126.greenimpact.model.Role;
import com.github.discovery126.greenimpact.model.User;
import com.github.discovery126.greenimpact.repository.UserRepository;
import com.github.discovery126.greenimpact.security.SecuritySessionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.discovery126.greenimpact.utils.UpdateUtils.updateFieldIfChanged;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final SecuritySessionContext securitySessionContext;

    private final CityService cityService;
    private final RoleService roleService;
    private final TaskService taskService;

    private final UserMapper userMapper;

    public void register(RegisterDto registerDto) {

        final String roleByDefault = "USER";
        final String cityDefault = "Таганрог";
        final City city = cityService.getCity(cityDefault);
        final Role role = roleService.getRole(roleByDefault);

        checkDisplayNameExistence(registerDto.getDisplayName());

        checkEmailExistence(registerDto.getEmail());

        Set<Role> userRoles = Set.of(role);

        User user = User.builder()
                .displayName(registerDto.getDisplayName())
                .email(registerDto.getEmail())
                .passwordHash(passwordEncoder.encode(registerDto.getPassword()))
                .roles(userRoles)
                .city(city)
                .build();

        userRepository.save(user);
    }
    @Transactional(readOnly = true)
    public Page<User> getAllUsers(int page, int size, String sort) {

        Sort sortOrder = Sort.by(sort.split(",")[0]);
        if (sort.split(",")[1].equalsIgnoreCase("desc")) {
            sortOrder = sortOrder.descending();
        } else {
            sortOrder = sortOrder.ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sortOrder);

        return userRepository.findAll(pageable);
    }

    public UserResponse createUser(UserRequest userRequest) {
        City city = cityService.getCity(userRequest.getCityId());

        Set<Role> roles = userRequest
                .getRoles()
                .stream()
                .map(roleService::getRole)
                .collect(Collectors.toSet());

        checkDisplayNameExistence(userRequest.getDisplayName());

        checkEmailExistence(userRequest.getEmail());

        User newUser = User.builder()
                .email(userRequest.getEmail())
                .displayName(userRequest.getDisplayName())
                .points(userRequest.getPoints())
                .city(city)
                .passwordHash(passwordEncoder.encode(userRequest.getPassword()))
                .roles(roles)
                .build();

        return userMapper.toResponse(userRepository.save(newUser));
    }

    public UserResponse updateUser(UserUpdateRequest userUpdateRequest, Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty())
            throw new UserNotFoundException("User not found");

        City city = cityService.getCity(userUpdateRequest.getCityId());

        Set<Role> roles = userUpdateRequest
                .getRoles()
                .stream()
                .map(roleService::getRole)
                .collect(Collectors.toSet());

        User user = userOptional.get();
        updateFieldIfChanged(user.getEmail(),userUpdateRequest.getEmail(), user::setEmail);
        updateFieldIfChanged(user.getDisplayName(),userUpdateRequest.getDisplayName(), user::setDisplayName);
        updateFieldIfChanged(user.getPoints(),userUpdateRequest.getPoints(), user::setPoints);
        updateFieldIfChanged(user.getRoles(),roles,user::setRoles);
        updateFieldIfChanged(user.getCity(),city,user::setCity);

        return userMapper.toResponse(userRepository.save(user));
    }

    public void deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new UserNotFoundException("Пользователь с id %d не найден".formatted(userId));
        }
    }

    private void checkEmailExistence(String email) {
        Optional<User> existedUser = userRepository.findByEmail(email);
        if (existedUser.isPresent())
            throw new UsernameAlreadyExistsException("Email is already registered");
    }

    private void checkDisplayNameExistence(String displayName) {
        Optional<User> existedUser = userRepository.findByDisplayName(displayName);
        if (existedUser.isPresent())
            throw new UsernameAlreadyExistsException("Username is already taken");
    }

    public List<TaskResponse> getTasksForCurrentUserOrAll() {
        if (!securitySessionContext.isUserLoggedIn()) {
            return taskService.getAllTasks();
        } else {
            return taskService.getTasksForCurrentUser(securitySessionContext.getId());
        }
    }

    public UserResponse getUser() {
        Optional<User> userOptional = userRepository.findById(securitySessionContext.getId());
        if (userOptional.isEmpty())
            throw new UserNotFoundException("Пользователя не существует");
        return userMapper.toResponse(userOptional.get());
    }

    public List<RatingResponse> getRatingMonth() {
        return userRepository.findRatingByStartDateAndEndDate(
                LocalDate.now().minusMonths(1),
                LocalDate.now()
        );
    }

    public List<RatingResponse> getRatingWeek() {
        return userRepository.findRatingByStartDateAndEndDate(
                LocalDate.now().minusWeeks(1),
                LocalDate.now()
        );
    }
}
