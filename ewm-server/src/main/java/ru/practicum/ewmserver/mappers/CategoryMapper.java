package ru.practicum.ewmserver.mappers;

import ru.practicum.ewmserver.dto.category.CategoryDto;
import ru.practicum.ewmserver.model.Category;

public class CategoryMapper {

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return new Category(
                categoryDto.getId() != null ? categoryDto.getId() : 0,
                categoryDto.getName() != null ? categoryDto.getName() : ""
        );
    }
}
