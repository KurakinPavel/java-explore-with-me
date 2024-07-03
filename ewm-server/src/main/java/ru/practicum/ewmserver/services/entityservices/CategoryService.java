package ru.practicum.ewmserver.services.entityservices;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewmserver.dto.category.CategoryDto;
import ru.practicum.ewmserver.exceptions.custom.CategoryValidationException;
import ru.practicum.ewmserver.mappers.CategoryMapper;
import ru.practicum.ewmserver.model.Category;
import ru.practicum.ewmserver.repositories.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryDto save(CategoryDto categoryDto) {
        if ((categoryDto.getName() == null) || (categoryDto.getName().isBlank())) {
            throw new CategoryValidationException("Переданы некорректные данные для создания категории");
        }
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
    }

    public CategoryDto update(int catId, CategoryDto categoryDto) {
        Category updatingCategory = getCategory(catId);
        if ((categoryDto.getName() == null) || (categoryDto.getName().isBlank())) {
            throw new CategoryValidationException("Переданы некорректные данные для создания категории");
        }
        if (updatingCategory.getName().equals(categoryDto.getName())) {
            categoryDto.setId(updatingCategory.getId());
            return categoryDto;
        }
        updatingCategory.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(updatingCategory));
    }

    public Category getCategory(int categoryId) {
        return categoryRepository.getReferenceById(categoryId);
    }

    public List<CategoryDto> getCategories(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return categoryRepository.findAll(pageable).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    public void deleteCategory(int catId) {
        Category category = getCategory(catId);
        category.getId();
        categoryRepository.delete(category);
    }
}
