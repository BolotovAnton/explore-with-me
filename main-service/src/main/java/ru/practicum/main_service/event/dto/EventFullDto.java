package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.event.enums.EventState;
import ru.practicum.main_service.user.dto.UserShortDto;
import ru.practicum.stats_dto.Constants;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

    private Long id;

    private String title;

    private String annotation;

    private String description;

    private CategoryDto category;

    private Boolean paid;

    private Long participantLimit;

    @JsonFormat(pattern = Constants.DATEFORMAT, shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;

    private LocationDto location;

    @JsonFormat(pattern = Constants.DATEFORMAT, shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdOn;

    private EventState state;

    @JsonFormat(pattern = Constants.DATEFORMAT, shape = JsonFormat.Shape.STRING)
    private LocalDateTime publishedOn;

    private UserShortDto initiator;

    private Boolean requestModeration;

    private Long confirmedRequests;

    private Long views;
}
