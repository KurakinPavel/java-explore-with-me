package ru.practicum.ewmserver.services.entityservices;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewmserver.dto.CategoryDto;
import ru.practicum.ewmserver.exceptions.custom.CategoryValidationException;
import ru.practicum.ewmserver.mappers.CategoryMapper;
import ru.practicum.ewmserver.model.Category;
import ru.practicum.ewmserver.model.User;
import ru.practicum.ewmserver.repositories.CategoryRepository;

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

    public Category getCategory(int categoryId) {
        return categoryRepository.getReferenceById(categoryId);
    }
}
