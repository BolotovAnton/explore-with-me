package ru.practicum.main_service.compilation.dto;

import lombok.*;
import ru.practicum.main_service.event.model.Event;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {

	private Long id;

	private Boolean pinned;

	private String title;

	private List<Event> events;
}