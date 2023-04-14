package ru.practicum.main_service.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.compilation.dto.CompilationCreateDto;
import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.dto.CompilationUpdateDto;
import ru.practicum.main_service.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Validated
public class AdminCompilationController {

	private final CompilationService compilationService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CompilationDto addCompilation(@Valid @RequestBody CompilationCreateDto compilationCreateDto) {
		return compilationService.addCompilation(compilationCreateDto);
	}

	@DeleteMapping("/{compId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCompilation(@PathVariable Long compId) {
		compilationService.deleteCompilation(compId);
	}

	@PatchMapping("/{compId}")
	@ResponseStatus(HttpStatus.OK)
	public CompilationDto updateCompilation(@PathVariable Long compId,
	                                        @Valid @RequestBody CompilationUpdateDto compilationUpdateDto) {
		return compilationService.updateCompilation(compId, compilationUpdateDto);
	}
}