package ru.practicum.main_service.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class PublicCategoryController {

	private final CategoryService categoryService;

	@GetMapping
	public List<CategoryDto> getCategories(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
										   @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		return categoryService.findCategories(from, size);
	}

	@GetMapping("/{catId}")
	public CategoryDto getCategory(@PathVariable Long catId) {
		return categoryService.findCategoryById(catId);
	}
}