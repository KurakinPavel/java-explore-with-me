package ru.practicum.ewmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {
    protected Integer id;
    @NotBlank
    @Size(min = 2, max = 250)
    protected String name;
    @NotBlank
    @Email
    @Size(min = 6, max = 254)
    protected String email;
}
