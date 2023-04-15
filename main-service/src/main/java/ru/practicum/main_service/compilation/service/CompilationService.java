package ru.practicum.main_service.compilation.service;

import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.dto.CompilationCreateDto;
import ru.practicum.main_service.compilation.dto.CompilationUpdateDto;

import java.util.List;

public interface CompilationService {

	CompilationDto addCompilation(CompilationCreateDto compilationCreateDto);

	void deleteCompilation(Long compId);

	CompilationDto updateCompilation(Long compId, CompilationUpdateDto compilationUpdateDto);

	List<CompilationDto> findCompilations(Boolean pinned, Integer from, Integer size);

	CompilationDto findCompilation(Long compId);
}