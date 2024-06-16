package ru.practicum.statsserverdto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HitDtoIn {
    protected Integer id;
    @NotBlank
    protected String app;
    @NotBlank
    protected String uri;
    @NotBlank
    protected String ip;
    protected String timestamp;
}
