package ru.practicum.main_service.event.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.main_service.category.mapper.MapperCategory;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.event.dto.EventCreateDto;
import ru.practicum.main_service.event.dto.EventFullDto;
import ru.practicum.main_service.event.dto.EventShortDto;
import ru.practicum.main_service.event.enums.EventState;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.model.Location;
import ru.practicum.main_service.event.service.StatsService;
import ru.practicum.main_service.user.mapper.MapperUser;
import ru.practicum.main_service.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MapperEvent {

    private final StatsService statsService;

    public Event toEvent(EventCreateDto eventCreateDto, User initiator, Category category,
                                Location location, LocalDateTime created,
                                EventState state) {
        Event event = new Event();
        event.setTitle(eventCreateDto.getTitle());
        event.setAnnotation(eventCreateDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(eventCreateDto.getDescription());
        event.setPaid(eventCreateDto.getPaid());
        event.setParticipantLimit(eventCreateDto.getParticipantLimit());
        event.setEventDate(eventCreateDto.getEventDate());
        event.setLocation(location);
        event.setCreatedOn(created);
        event.setState(state);
        event.setInitiator(initiator);
        event.setRequestModeration(eventCreateDto.getRequestModeration());
        return event;
    }

    public EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(MapperCategory.toCategoryDto(event.getCategory()));
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setLocation(MapperLocation.toLocationDto(event.getLocation()));
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setState(event.getState());
        eventFullDto.setInitiator(MapperUser.toUserShortDto(event.getInitiator()));
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setConfirmedRequests(statsService.getConfirmedRequests(event).getOrDefault(event.getId(), 0L));
        eventFullDto.setViews(statsService.getViews(event).getOrDefault(event.getId(), 0L));
        return eventFullDto;
    }

    public List<EventFullDto> toEventFullDto(List<Event> events) {
        return events.stream().map(this::toEventFullDto).collect(Collectors.toList());
    }

    public EventShortDto toEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(MapperCategory.toCategoryDto(event.getCategory()));
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setInitiator(MapperUser.toUserShortDto(event.getInitiator()));
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setConfirmedRequests(statsService.getConfirmedRequests(event).getOrDefault(event.getId(), 0L));
        eventShortDto.setViews(statsService.getViews(event).getOrDefault(event.getId(), 0L));
        return eventShortDto;
    }

    public List<EventShortDto> toEventShortDto(List<Event> events) {
        return events.stream().map(this::toEventShortDto).collect(Collectors.toList());
    }
}
