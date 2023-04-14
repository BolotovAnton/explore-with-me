package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.stats_dto.Constants;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateDto {

    @NotBlank
    @Size(min = 3, max = 200)
    private String title;

    @NotBlank
    @Size(min = 20, max = 3000)
    private String annotation;

    @NotBlank
    @Size(min = 20, max = 8000)
    private String description;

    @NotNull
    private Long category;

    private Boolean paid = false;

    @PositiveOrZero
    private Long participantLimit = 0L;

    @NotNull
    @JsonFormat(pattern = Constants.DATEFORMAT, shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;

    @NotNull
    @Valid
    private LocationDto location;

    private Boolean requestModeration = true;
}
