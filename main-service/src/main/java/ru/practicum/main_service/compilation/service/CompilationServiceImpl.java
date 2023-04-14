package ru.practicum.main_service.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.compilation.dao.CompilationRepository;
import ru.practicum.main_service.compilation.dto.CompilationCreateDto;
import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.dto.CompilationUpdateDto;
import ru.practicum.main_service.compilation.mapper.MapperCompilation;
import ru.practicum.main_service.compilation.model.Compilation;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.service.EventService;
import ru.practicum.main_service.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE)
public class CompilationServiceImpl implements CompilationService {

    private final EventService eventService;
    private final CompilationRepository compilationRepository;
    private final MapperCompilation mapperCompilation;

    @Override
    public CompilationDto addCompilation(CompilationCreateDto compilationCreateDto) {
        if (!compilationCreateDto.getEvents().isEmpty()) {
            List<Event> events = eventService.getEventsByIds(compilationCreateDto.getEvents());
            if (compilationCreateDto.getEvents().size() != events.size()) {
                throw new NotFoundException("event is not found");
            }
        }
        CompilationDto compilationDto = mapperCompilation.toCompilationDto(
                compilationRepository.save(mapperCompilation.toCompilation(compilationCreateDto)));
        log.info("compilation added");
        return compilationDto;
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("compilation is not found")
        );
        compilationRepository.deleteById(compId);
        log.info("compilation with id = " + compId + " has been deleted");
    }

    @Override
    public CompilationDto updateCompilation(Long compId, CompilationUpdateDto compilationUpdateDto) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("compilation is not found"));
        if (compilationUpdateDto.getTitle() != null) {
            compilation.setTitle(compilationUpdateDto.getTitle());
        }
        if (compilationUpdateDto.getPinned() != null) {
            compilation.setPinned(compilationUpdateDto.getPinned());
        }
        if (compilationUpdateDto.getEvents() != null) {
            List<Event> events = eventService.getEventsByIds(compilationUpdateDto.getEvents());
            if (events.size() != compilationUpdateDto.getEvents().size()) {
                throw new NotFoundException("some events have been not found");
            }
            compilation.setEvents(events);
        }
        compilationRepository.save(compilation);
        log.info("compilation with id = " + compId + " has been updated");
        return findCompilation(compId);
    }

    @Override
    public List<CompilationDto> findCompilations(Boolean pinned, Integer from, Integer size) {
        List<CompilationDto> compilationDtoList = new ArrayList<>();
        if (pinned != null) {
            List<Compilation> pinnedCompilations = compilationRepository.findByPinned(
                    pinned, PageRequest.of(from / size, size)
            );
            pinnedCompilations.forEach(x -> compilationDtoList.add(mapperCompilation.toCompilationDto(x)));
        } else {
            compilationRepository.findAll(PageRequest.of(from / size, size))
                    .forEach(x -> compilationDtoList.add(mapperCompilation.toCompilationDto(x)));
        }
        log.info("compilations have been found" + compilationDtoList);
        return compilationDtoList;
    }

    @Override
    public CompilationDto findCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("compilation is not found"));
        log.info("compilation has been found " + compilation);
        return mapperCompilation.toCompilationDto(compilation);
    }
}