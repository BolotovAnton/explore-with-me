package ru.practicum.main_service.category.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.category.dto.CategoryCreateDto;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.category.model.Category;

@UtilityClass
public class MapperCategory {

    public static Category toCategory(CategoryCreateDto categoryCreateDto) {
        Category category = new Category();
        category.setName(categoryCreateDto.getName());
        return category;
    }

    public static CategoryDto toCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }
}
