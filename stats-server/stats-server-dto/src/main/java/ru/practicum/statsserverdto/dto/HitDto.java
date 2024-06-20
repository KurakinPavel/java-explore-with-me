package ru.practicum.statsserverdto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class HitDto {
    protected Integer id;
    @NotBlank
    @Size(min = 5)
    protected String app;
    @NotBlank
    @Size(min = 5)
    protected String uri;
    @NotBlank
    @Size(min = 5)
    protected String ip;
    @NotBlank
    @Size(min = 19)
    protected String timestamp;
}
