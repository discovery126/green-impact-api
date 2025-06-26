package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.exception.CustomException;
import com.github.discovery126.greenimpact.exception.ValidationConstants;
import com.github.discovery126.greenimpact.model.Event;
import com.github.discovery126.greenimpact.model.User;
import com.github.discovery126.greenimpact.model.UserEvent;
import com.github.discovery126.greenimpact.repository.EventRepository;
import com.github.discovery126.greenimpact.repository.UserEventRepository;
import com.github.discovery126.greenimpact.repository.UserRepository;
import com.github.discovery126.greenimpact.security.SecuritySessionContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserEventRepository userEventRepository;

    @Mock
    private SecuritySessionContext securitySessionContext;

    @Test
    void shouldRegisterEventSuccessfully() {
        Long eventId = 1L;
        Long userId = 42L;

        Event event = Event.builder().id(eventId).build();
        User user = User.builder().id(userId).build();

        when(securitySessionContext.getId()).thenReturn(userId);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userEventRepository.existsByUserIdAndEventId(userId, eventId)).thenReturn(false);

        eventService.registerEvent(eventId);

        verify(securitySessionContext, times(2)).getId();
        verify(eventRepository).findById(eventId);
        verify(userRepository).findById(userId);
        verify(userEventRepository).existsByUserIdAndEventId(userId, eventId);
        verify(userEventRepository).save(any(UserEvent.class));
        verifyNoMoreInteractions(eventRepository, userRepository, userEventRepository, securitySessionContext);
    }

    @Test
    void shouldThrowIfUserAlreadyRegistered() {
        Long eventId = 1L;
        Long userId = 42L;

        Event event = Event.builder().id(eventId).build();
        User user = User.builder().id(userId).build();

        when(securitySessionContext.getId()).thenReturn(userId);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userEventRepository.existsByUserIdAndEventId(userId, eventId)).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> eventService.registerEvent(eventId));
        assertEquals(ValidationConstants.EVENT_USER_ALREADY_REGISTERED, exception.getMessage());

        verify(securitySessionContext, times(2)).getId();
        verify(eventRepository).findById(eventId);
        verify(userRepository).findById(userId);
        verify(userEventRepository).existsByUserIdAndEventId(userId, eventId);
        verifyNoMoreInteractions(userEventRepository);
    }

    @Test
    void shouldThrowIfEventNotFound() {
        Long eventId = 1L;

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> eventService.registerEvent(eventId));
        assertEquals(ValidationConstants.EVENT_ID_NOT_FOUND, exception.getMessage());

        verify(eventRepository).findById(eventId);
        verifyNoMoreInteractions(eventRepository);
    }

    @Test
    void shouldThrowIfUserNotFound() {
        Long eventId = 1L;
        Long userId = 42L;
        Event event = Event.builder().id(eventId).build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(securitySessionContext.getId()).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> eventService.registerEvent(eventId));
        assertEquals(ValidationConstants.USER_NOT_FOUND, exception.getMessage());

        verify(securitySessionContext).getId();
        verify(eventRepository).findById(eventId);
        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }
}
