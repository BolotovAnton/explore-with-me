package ru.practicum.main_service.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.event.dto.ParticipationRequestDto;
import ru.practicum.main_service.event.dto.UpdateRequestStatusRequest;
import ru.practicum.main_service.event.dto.UpdateRequestStatusResponse;
import ru.practicum.main_service.event.enums.EventState;
import ru.practicum.main_service.event.enums.RequestStatus;
import ru.practicum.main_service.event.enums.RequestStatusAction;
import ru.practicum.main_service.event.mapper.MapperRequest;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.model.Request;
import ru.practicum.main_service.event.repository.RequestRepository;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.exception.WrongTermsException;
import ru.practicum.main_service.user.model.User;
import ru.practicum.main_service.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final UserService userService;
    private final EventService eventService;
    private final StatsService statsService;
    private final RequestRepository requestRepository;

    @Override
    public List<ParticipationRequestDto> findRequestsByRequester(Long userId) {
        userService.findUserById(userId);
        List<ParticipationRequestDto> participationRequestDtoList = MapperRequest.toParticipationRequestDto(
                requestRepository.findAllByRequesterId(userId)
        );
        log.info("requests to participate in other user's events have been found");
        return participationRequestDtoList;
    }

    @Override
    @Transactional
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        User user = userService.findUserById(userId);
        Event event = eventService.getEventById(eventId);
        if (event.getInitiator().getId().equals(userId)) {
            throw new WrongTermsException("user with id = " + userId + " can't add a request for event with id = " + eventId);
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new WrongTermsException("event is not published");
        }
        Optional<Request> savedRequest = requestRepository.findByEventIdAndRequesterId(eventId, userId);
        if (savedRequest.isPresent()) {
            throw new WrongTermsException("request is already exist");
        }
        checkParticipantLimit(
                statsService.getConfirmedRequests(Collections.singletonList(event))
                        .getOrDefault(eventId, 0L) + 1,
                event.getParticipantLimit()
        );
        Request request = new Request();
        request.setEvent(event);
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }
        log.info("requesr for event with id = " + eventId + " has been added");
        return MapperRequest.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        userService.findUserById(userId);
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("request is not found"));
        if (!userId.equals(request.getRequester().getId())) {
            throw new WrongTermsException("user is not the requester");
        }
        request.setStatus(RequestStatus.CANCELED);
        log.info("Отмена запроса с id {} на участие в событии пользователем с id {}", requestId, userId);
        return MapperRequest.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> findRequestsByEventAuthor(Long userId, Long eventId) {
        log.info("Вывод списка запросов на участие в событии с id {} владельцем с id {}", eventId, userId);

        userService.findUserById(userId);
        Event event = eventService.getEventById(eventId);
        if (!userId.equals(event.getInitiator().getId())) {
            throw new WrongTermsException("user is not the initiator");
        }
        return MapperRequest.toParticipationRequestDto(requestRepository.findAllByEventId(eventId));
    }

    @Override
    @Transactional
    public UpdateRequestStatusResponse updateRequestsByEventAuthor(Long userId,
                                                                   Long eventId,
                                                                   UpdateRequestStatusRequest updateRequestStatusRequest) {
        userService.findUserById(userId);
        Event event = eventService.getEventById(eventId);
        if (!userId.equals(event.getInitiator().getId())) {
            throw new WrongTermsException("user is not the initiator");
        }
        if (!event.getRequestModeration() ||
                event.getParticipantLimit() == 0 ||
                updateRequestStatusRequest.getRequestIds().isEmpty()) {
            return new UpdateRequestStatusResponse(List.of(), List.of());
        }
        List<Request> confirmedList = new ArrayList<>();
        List<Request> rejectedList = new ArrayList<>();
        List<Request> requests = requestRepository.findAllByIdIn(updateRequestStatusRequest.getRequestIds());
        if (requests.size() != updateRequestStatusRequest.getRequestIds().size()) {
            throw new NotFoundException("Некоторые запросы на участие не найдены.");
        }
        if (!requests.stream()
                .map(Request::getStatus)
                .allMatch(RequestStatus.PENDING::equals)) {
            throw new WrongTermsException("Изменять можно только заявки, находящиеся в ожидании.");
        }
        if (updateRequestStatusRequest.getStatus().equals(RequestStatusAction.REJECTED)) {
            rejectedList.addAll(changeRequestsStatusAndSave(requests, RequestStatus.REJECTED));
        } else {
            Long newConfirmedRequests = statsService.getConfirmedRequests(List.of(event)).getOrDefault(eventId, 0L) +
                    updateRequestStatusRequest.getRequestIds().size();
            checkParticipantLimit(newConfirmedRequests, event.getParticipantLimit());
            confirmedList.addAll(changeRequestsStatusAndSave(requests, RequestStatus.CONFIRMED));
            if (newConfirmedRequests >= event.getParticipantLimit()) {
                rejectedList.addAll(changeRequestsStatusAndSave(
                        requestRepository.findAllByEventIdAndStatus(eventId, RequestStatus.PENDING),
                        RequestStatus.REJECTED)
                );
            }
        }
        UpdateRequestStatusResponse updateRequestStatusResponse = new UpdateRequestStatusResponse(
                MapperRequest.toParticipationRequestDto(confirmedList),
                MapperRequest.toParticipationRequestDto(rejectedList)
        );
        log.info("requests have been updated");
        return updateRequestStatusResponse;
    }

    private List<Request> changeRequestsStatusAndSave(List<Request> requests, RequestStatus status) {
        requests.forEach(request -> request.setStatus(status));
        return requestRepository.saveAll(requests);
    }

    private void checkParticipantLimit(Long limit, Long eventParticipantLimit) {
        if (eventParticipantLimit != 0 && (limit > eventParticipantLimit)) {
            throw new WrongTermsException("limit for participation is reached");
        }
    }
}
