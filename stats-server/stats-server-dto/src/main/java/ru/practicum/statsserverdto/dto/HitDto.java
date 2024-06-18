package ru.practicum.statsserverdto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class HitDto {
    protected Integer id;
    @NotBlank
    protected String app;
    @NotBlank
    protected String uri;
    @NotBlank
    protected String ip;
    @NotBlank
    protected String timestamp;
}
