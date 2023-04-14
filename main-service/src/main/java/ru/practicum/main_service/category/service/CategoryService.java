package ru.practicum.main_service.category.service;

import ru.practicum.main_service.category.dto.CategoryCreateDto;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.category.model.Category;

import java.util.List;

public interface CategoryService {

	CategoryDto addCategory(CategoryCreateDto categoryCreateDto);

	CategoryDto updateCategory(Long catId, CategoryDto categoryDto);

	List<CategoryDto> findCategories(Integer from, Integer size);

	CategoryDto findCategoryById(Long catId);

	void deleteCategory(Long catId);

	Category getCategoryById(Long catId);
}