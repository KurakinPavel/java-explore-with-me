package ru.practicum.ewmserver.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserDto {
    protected Integer id;
    @NotBlank
    @Size(min = 5)
    protected String name;
    @Email
    protected String email;
}