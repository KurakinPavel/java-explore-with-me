package ru.practicum.ewmserver.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserShortDto {
    protected Integer id;
    @NotBlank
    @Size(min = 5)
    protected String name;
}
