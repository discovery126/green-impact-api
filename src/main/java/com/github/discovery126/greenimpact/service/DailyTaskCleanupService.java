package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.model.TakenTask;
import com.github.discovery126.greenimpact.model.TaskType;
import com.github.discovery126.greenimpact.model.User;
import com.github.discovery126.greenimpact.repository.TakenTaskRepository;
import com.github.discovery126.greenimpact.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DailyTaskCleanupService implements TaskCleanupService {


    private final UserRepository userRepository;
    private final TakenTaskRepository takenTaskRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void cleanupExpiredTasks() {
        try (Stream<User> users = userRepository.streamAll()) {
            users.forEach(user -> {
                List<TakenTask> dailyTasks = takenTaskRepository
                        .findAllTakenTaskByUserIdAndType(user.getId(), TaskType.DAILY.name());
                takenTaskRepository.deleteAll(dailyTasks);
            });
        }
    }
}
