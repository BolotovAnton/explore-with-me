package ru.practicum.main_service.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.category.service.CategoryService;
import ru.practicum.main_service.event.dto.*;
import ru.practicum.main_service.event.enums.EventSort;
import ru.practicum.main_service.event.enums.EventState;
import ru.practicum.main_service.event.enums.EventStateAction;
import ru.practicum.main_service.event.mapper.EventMapper;
import ru.practicum.main_service.event.mapper.LocationMapper;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.model.Location;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.event.repository.LocationRepository;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.exception.WrongTermsException;
import ru.practicum.main_service.user.model.User;
import ru.practicum.main_service.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EventServiceImpl implements EventService {

    private final UserService userService;
    private final CategoryService categoryService;
    private final StatsService statsService;
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public List<EventFullDto> findEventsByAdmin(List<Long> users,
                                                List<EventState> states,
                                                List<Long> categories,
                                                LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd,
                                                Integer from,
                                                Integer size) {
        checkTimeRange(rangeStart, rangeEnd);
        List<Event> events = eventRepository.findEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
        log.info("events have been found");
        return eventMapper.toEventFullDto(events);
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, EventUpdateAdminDto eventUpdateAdminDto) {
        if (eventUpdateAdminDto.getEventDate() != null &&
                eventUpdateAdminDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new WrongTermsException("Wrong eventDate");
        }
        Event event = getEventById(eventId);
        if (eventUpdateAdminDto.getTitle() != null) {
            event.setTitle(eventUpdateAdminDto.getTitle());
        }
        if (eventUpdateAdminDto.getAnnotation() != null) {
            event.setAnnotation(eventUpdateAdminDto.getAnnotation());
        }

        if (eventUpdateAdminDto.getDescription() != null) {
            event.setDescription(eventUpdateAdminDto.getDescription());
        }
        if (eventUpdateAdminDto.getCategory() != null) {
            event.setCategory(categoryService.getCategoryById(eventUpdateAdminDto.getCategory()));
        }
        if (eventUpdateAdminDto.getPaid() != null) {
            event.setPaid(eventUpdateAdminDto.getPaid());
        }
        if (eventUpdateAdminDto.getParticipantLimit() != null) {
            Long confirmedRequests = statsService.getConfirmedRequests(Collections.singletonList(event))
                    .getOrDefault(eventId, 0L);
            if (eventUpdateAdminDto.getParticipantLimit() != 0 && confirmedRequests != 0 &&
                    (eventUpdateAdminDto.getParticipantLimit() < confirmedRequests)) {
                throw new WrongTermsException("wrong participation limit");
            }
            event.setParticipantLimit(eventUpdateAdminDto.getParticipantLimit());
        }
        if (eventUpdateAdminDto.getEventDate() != null) {
            event.setEventDate(eventUpdateAdminDto.getEventDate());
        }
        if (eventUpdateAdminDto.getLocation() != null) {
            Location location = locationRepository.findByLatAndLon(
                    eventUpdateAdminDto.getLocation().getLat(), eventUpdateAdminDto.getLocation().getLon()
            ).orElseGet(() -> locationRepository.save(LocationMapper.toLocation(eventUpdateAdminDto.getLocation())));
            event.setLocation(location);
        }
        if (eventUpdateAdminDto.getStateAction() != null) {
            if (!event.getState().equals(EventState.PENDING)) {
                throw new WrongTermsException("event is not pending");
            }
            if (eventUpdateAdminDto.getStateAction().equals(EventStateAction.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (eventUpdateAdminDto.getStateAction().equals(EventStateAction.REJECT_EVENT)) {
                event.setState(EventState.REJECTED);
            }
        }
        if (eventUpdateAdminDto.getRequestModeration() != null) {
            event.setRequestModeration(eventUpdateAdminDto.getRequestModeration());
        }
        log.info("event with id = " + eventId + " has been updated");
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> findAllEventsByPrivate(Long userId, Pageable pageable) {
        userService.findUserById(userId);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);
        log.info("events of user with id = " + userId + " have been found");
        return eventMapper.toEventShortDto(events);
    }

    @Override
    @Transactional
    public EventFullDto addEventByPrivate(Long userId, EventCreateDto eventCreateDto) {
        if (eventCreateDto.getEventDate() != null &&
                eventCreateDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new WrongTermsException("Wrong eventDate");
        }
        User user = userService.findUserById(userId);
        Category category = categoryService.getCategoryById(eventCreateDto.getCategory());
        Location location = locationRepository.findByLatAndLon(
                eventCreateDto.getLocation().getLat(), eventCreateDto.getLocation().getLon()
        ).orElseGet(() -> locationRepository.save(LocationMapper.toLocation(eventCreateDto.getLocation())));

        Event savedEvent = eventMapper.toEvent(
                eventCreateDto, user, category, location, LocalDateTime.now(), EventState.PENDING
        );
        log.info("event has been added");
        return eventMapper.toEventFullDto(eventRepository.save(savedEvent));
    }

    @Override
    public EventFullDto findEventByPrivate(Long userId, Long eventId) {
        userService.findUserById(userId);
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("event is not found"));
        log.info("event with id = " + eventId + " has been found");
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventFullDto updateEventByPrivate(Long userId, Long eventId, EventUpdateUserDto eventUpdateUserDto) {
        if (eventUpdateUserDto.getEventDate() != null &&
                eventUpdateUserDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new WrongTermsException("Wrong eventDate");
        }
        userService.findUserById(userId);
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("event is not found"));
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new WrongTermsException("event is already published");
        }
        if (eventUpdateUserDto.getTitle() != null) {
            event.setTitle(eventUpdateUserDto.getTitle());
        }
        if (eventUpdateUserDto.getAnnotation() != null) {
            event.setAnnotation(eventUpdateUserDto.getAnnotation());
        }
        if (eventUpdateUserDto.getDescription() != null) {
            event.setDescription(eventUpdateUserDto.getDescription());
        }
        if (eventUpdateUserDto.getCategory() != null) {
            event.setCategory(categoryService.getCategoryById(eventUpdateUserDto.getCategory()));
        }
        if (eventUpdateUserDto.getPaid() != null) {
            event.setPaid(eventUpdateUserDto.getPaid());
        }
        if (eventUpdateUserDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUpdateUserDto.getParticipantLimit());
        }
        if (eventUpdateUserDto.getEventDate() != null) {
            event.setEventDate(eventUpdateUserDto.getEventDate());
        }
        if (eventUpdateUserDto.getLocation() != null) {
            Location location = locationRepository.findByLatAndLon(
                    eventUpdateUserDto.getLocation().getLat(), eventUpdateUserDto.getLocation().getLon()
            ).orElseGet(() -> locationRepository.save(LocationMapper.toLocation(eventUpdateUserDto.getLocation())));
            event.setLocation(location);
        }
        if (eventUpdateUserDto.getStateAction() != null) {
            if (eventUpdateUserDto.getStateAction().equals(EventStateAction.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            } else if (eventUpdateUserDto.getStateAction().equals(EventStateAction.CANCEL_REVIEW)) {
                event.setState(EventState.CANCELED);
            }
        }
        if (eventUpdateUserDto.getRequestModeration() != null) {
            event.setRequestModeration(eventUpdateUserDto.getRequestModeration());
        }
        log.info("event with id = " + eventId + " has been updated");
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> findEventsByPublic(String text,
                                                  List<Long> categories,
                                                  Boolean paid,
                                                  LocalDateTime rangeStart,
                                                  LocalDateTime rangeEnd,
                                                  Boolean onlyAvailable,
                                                  EventSort sort,
                                                  Integer from,
                                                  Integer size,
                                                  HttpServletRequest request) {
        checkTimeRange(rangeStart, rangeEnd);
        List<Event> events = eventRepository.findEventsByPublic(text, categories, paid, rangeStart, rangeEnd, from, size);
        if (events.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, Long> eventsParticipantLimit = new HashMap<>();
        events.forEach(event -> eventsParticipantLimit.put(event.getId(), event.getParticipantLimit()));
        List<EventShortDto> eventsShortDto = eventMapper.toEventShortDto(events);
        if (onlyAvailable) {
            eventsShortDto = eventsShortDto.stream()
                    .filter(event -> (eventsParticipantLimit.get(event.getId()) == 0 ||
                            eventsParticipantLimit.get(event.getId()) > event.getConfirmedRequests()))
                    .collect(Collectors.toList());
        }
        if (sort != null && sort.equals(EventSort.VIEWS)) {
            eventsShortDto.sort(Comparator.comparing(EventShortDto::getViews));
        } else if (sort != null && sort.equals(EventSort.EVENT_DATE)) {
            eventsShortDto.sort(Comparator.comparing(EventShortDto::getEventDate));
        }
        statsService.addHit(request);
        log.info("events have been found");
        return eventsShortDto;
    }

    @Override
    public EventFullDto findEventByPublic(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("event is not found"));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("event is not published ");
        }
        statsService.addHit(request);
        log.info("event with id = " + eventId + " has been found");
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("event is not found"));
    }

    @Override
    public List<Event> getEventsByIds(List<Long> eventsId) {
        if (eventsId.isEmpty()) {
            return Collections.emptyList();
        }
        return eventRepository.findAllByIdIn(eventsId);
    }

    private void checkTimeRange(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new WrongTermsException("invalid time period");
        }
    }
}
