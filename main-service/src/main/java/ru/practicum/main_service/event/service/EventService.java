package ru.practicum.main_service.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.main_service.event.dto.EventFullDto;
import ru.practicum.main_service.event.dto.EventShortDto;
import ru.practicum.main_service.event.dto.EventCreateDto;
import ru.practicum.main_service.event.dto.EventUpdateAdminDto;
import ru.practicum.main_service.event.dto.EventUpdateUserDto;
import ru.practicum.main_service.event.enums.EventSort;
import ru.practicum.main_service.event.enums.EventState;
import ru.practicum.main_service.event.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventFullDto> findEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto updateEventByAdmin(Long eventId, EventUpdateAdminDto eventUpdateAdminDto);

    List<EventShortDto> findAllEventsByPrivate(Long userId, Pageable pageable);

    EventFullDto addEventByPrivate(Long userId, EventCreateDto eventCreateDto);

    EventFullDto findEventByPrivate(Long userId, Long eventId);

    EventFullDto updateEventByPrivate(Long userId, Long eventId, EventUpdateUserDto eventUpdateUserDto);

    List<EventShortDto> findEventsByPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort,
                                           Integer from, Integer size, HttpServletRequest request);

    EventFullDto findEventByPublic(Long id, HttpServletRequest request);

    Event getEventById(Long eventId);

    List<Event> getEventsByIds(List<Long> eventsId);
}
