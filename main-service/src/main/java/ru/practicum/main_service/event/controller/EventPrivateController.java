package ru.practicum.main_service.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.event.dto.*;
import ru.practicum.main_service.event.service.EventService;
import ru.practicum.main_service.event.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
@Validated
public class EventPrivateController {

    private final EventService eventService;
    private final RequestService requestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> findAllEventsByPrivate(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        return eventService.findAllEventsByPrivate(userId, PageRequest.of(from / size, size));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEventByPrivate(@PathVariable Long userId, @Valid @RequestBody EventCreateDto eventCreateDto) {
        return eventService.addEventByPrivate(userId, eventCreateDto);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto findEventByPrivate(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.findEventByPrivate(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventByPrivate(@PathVariable Long userId,
                                            @PathVariable Long eventId,
                                            @Valid @RequestBody EventUpdateUserDto eventUpdateUserDto) {
        return eventService.updateEventByPrivate(userId, eventId, eventUpdateUserDto);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> findRequestsByEventAuthor(@PathVariable Long userId,
                                                                      @PathVariable Long eventId) {
        return requestService.findRequestsByEventAuthor(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public UpdateRequestStatusResponse updateRequestsByEventAuthor(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateRequestStatusRequest updateRequestStatusRequest) {
        return requestService.updateRequestsByEventAuthor(userId, eventId, updateRequestStatusRequest);
    }
}
