package ru.practicum.main_service.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.category.dto.CategoryCreateDto;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Validated
public class AdminCategoryController {

	private final CategoryService categoryService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CategoryDto addCategory(@Valid @RequestBody CategoryCreateDto categoryCreateDto) {
		return categoryService.addCategory(categoryCreateDto);
	}

	@DeleteMapping("/{catId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCategory(@PathVariable Long catId) {
		categoryService.deleteCategory(catId);
	}

	@PatchMapping("/{catId}")
	@ResponseStatus(HttpStatus.OK)
	public CategoryDto updateCategory(@PathVariable Long catId, @Valid @RequestBody CategoryDto categoryDto) {
		return categoryService.updateCategory(catId, categoryDto);
	}
}