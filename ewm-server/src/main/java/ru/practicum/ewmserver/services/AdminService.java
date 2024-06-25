package ru.practicum.ewmserver.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmserver.dto.CategoryDto;
import ru.practicum.ewmserver.dto.UserDto;
import ru.practicum.ewmserver.services.entityservices.CategoryService;
import ru.practicum.ewmserver.services.entityservices.UserService;

@Slf4j
@Service
@AllArgsConstructor
public class AdminService {
    private final UserService userService;
    private final CategoryService categoryService;

    @Transactional
    public UserDto save(UserDto userDto) {
        return userService.save(userDto);
    }

    @Transactional
    public CategoryDto save(CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }
}
