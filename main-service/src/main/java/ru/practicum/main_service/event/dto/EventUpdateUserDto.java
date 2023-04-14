package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.main_service.event.enums.EventStateAction;
import ru.practicum.stats_dto.Constants;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventUpdateUserDto {

    @Size(min = 3, max = 200)
    String title;

    @Size(min = 20, max = 8000)
    String annotation;

    @Size(min = 20, max = 8000)
    String description;

    Long category;

    Boolean paid;

    @PositiveOrZero
    Long participantLimit;

    @JsonFormat(pattern = Constants.DATEFORMAT, shape = JsonFormat.Shape.STRING)
    LocalDateTime eventDate;

    @Valid
    LocationDto location;

    Boolean requestModeration;

    EventStateAction stateAction;
}
