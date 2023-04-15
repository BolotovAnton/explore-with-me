package ru.practicum.main_service.event.mapper;


import ru.practicum.main_service.event.dto.ParticipationRequestDto;
import ru.practicum.main_service.event.model.Request;

import java.util.ArrayList;
import java.util.List;

public class MapperRequest {

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setId(request.getId());
        participationRequestDto.setEvent(request.getEvent().getId());
        participationRequestDto.setRequester(request.getRequester().getId());
        participationRequestDto.setCreated(request.getCreated());
        participationRequestDto.setStatus(request.getStatus());
        return participationRequestDto;
    }

    public static List<ParticipationRequestDto> toParticipationRequestDto(List<Request> requestList) {
        List<ParticipationRequestDto> participationRequestDtoList = new ArrayList<>();
        for (Request request : requestList) {
            participationRequestDtoList.add(toParticipationRequestDto(request));
        }
        return participationRequestDtoList;
    }
}
