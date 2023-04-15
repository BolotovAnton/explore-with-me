package ru.practicum.main_service.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.category.dao.CategoryRepository;
import ru.practicum.main_service.category.dto.CategoryCreateDto;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.category.mapper.MapperCategory;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	@Override
	@Transactional
	public CategoryDto addCategory(CategoryCreateDto categoryCreateDto) {
		CategoryDto responseCategoryDto = MapperCategory.toCategoryDto(
				categoryRepository.save(MapperCategory.toCategory(categoryCreateDto))
		);
		log.info("Category has been added " + responseCategoryDto);
		return responseCategoryDto;
	}

	@Override
	@Transactional
	public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
		Category category = categoryRepository.findById(catId).orElseThrow(
				() -> new NotFoundException("category not found")
		);
		category.setName(categoryDto.getName());
		CategoryDto responseCategoryDto = MapperCategory.toCategoryDto(categoryRepository.save(category));
		log.info("category has been updated");
		return responseCategoryDto;
	}

	@Override
	@Transactional(readOnly = true)
	public List<CategoryDto> findCategories(Integer from, Integer size) {
		List<CategoryDto> categoryDtoList = new ArrayList<>();
		categoryRepository.findAll(PageRequest.of(from / size, size))
				.forEach(x -> categoryDtoList.add(MapperCategory.toCategoryDto(x)));
		log.info("list of categories have been gotten");
		return categoryDtoList;
	}

	@Override
	@Transactional(readOnly = true)
	public CategoryDto findCategoryById(Long catId) {
		CategoryDto categoryDto = MapperCategory.toCategoryDto(categoryRepository.findById(catId)
				.orElseThrow(() -> new NotFoundException("category not found")));
		log.info("category with id = " + catId + " has been found");
		return categoryDto;
	}

	@Override
	@Transactional
	public void deleteCategory(Long catId) {
		categoryRepository.delete(categoryRepository.findById(catId).orElseThrow(
				() -> new NotFoundException("category not found"))
		);
		log.info("category has been deleted");
	}

	@Override
	public Category getCategoryById(Long catId) {
		Category category = categoryRepository.findById(catId)
				.orElseThrow(() -> new NotFoundException("category not found"));
		log.info("category with id = " + catId + " has been found");
		return category;
	}
}
