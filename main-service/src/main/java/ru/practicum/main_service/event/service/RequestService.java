package ru.practicum.main_service.event.service;

import ru.practicum.main_service.event.dto.UpdateRequestStatusRequest;
import ru.practicum.main_service.event.dto.UpdateRequestStatusResponse;
import ru.practicum.main_service.event.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    List<ParticipationRequestDto> findRequestsByRequester(Long userId);

    ParticipationRequestDto addRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> findRequestsByEventAuthor(Long userId, Long eventId);

    UpdateRequestStatusResponse updateRequestsByEventAuthor(Long userId,
                                                            Long eventId,
                                                            UpdateRequestStatusRequest updateRequestStatusRequest);
}
