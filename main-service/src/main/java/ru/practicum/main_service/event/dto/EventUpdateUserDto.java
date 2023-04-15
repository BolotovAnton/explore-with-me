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
    private String title;

    @Size(min = 20, max = 8000)
    private String annotation;

    @Size(min = 20, max = 8000)
    private String description;

    private Long category;

    private Boolean paid;

    @PositiveOrZero
    private Long participantLimit;

    @JsonFormat(pattern = Constants.DATEFORMAT, shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;

    @Valid
    private LocationDto location;

    private Boolean requestModeration;

    private EventStateAction stateAction;
}
