package ru.practicum.ewmserver.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NewCategoryDto {
    @NotBlank
    @Size(min = 1, max = 50)
    protected String name;
}
