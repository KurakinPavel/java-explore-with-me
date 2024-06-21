package ru.practicum.ewmserver.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NewUserRequest {
    protected Integer id;
    @NotBlank
    @Size(min = 6, max = 254)
    @Email
    protected String email;
    @NotBlank
    @Size(min = 2, max = 250)
    protected String name;
}
