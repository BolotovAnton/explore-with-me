package ru.practicum.main_service.compilation.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.main_service.compilation.dto.CompilationCreateDto;
import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.model.Compilation;
import ru.practicum.main_service.event.repository.EventRepository;

@Component
@RequiredArgsConstructor
public class MapperCompilation {

    private final EventRepository eventRepository;

    public Compilation toCompilation(CompilationCreateDto compilationCreateDto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(compilationCreateDto.getTitle());
        compilation.setPinned(compilationCreateDto.getPinned());
        compilation.setEvents(eventRepository.findAllByIdIn(compilationCreateDto.getEvents()));
        return compilation;
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setEvents(compilation.getEvents());
        return compilationDto;
    }
}
