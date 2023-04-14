package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main_service.event.enums.RequestStatus;
import ru.practicum.stats_dto.Constants;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {

    private Long id;

    private Long event;

    private Long requester;

    @JsonFormat(pattern = Constants.DATEFORMAT, shape = JsonFormat.Shape.STRING)
    private LocalDateTime created;

    private RequestStatus status;
}
