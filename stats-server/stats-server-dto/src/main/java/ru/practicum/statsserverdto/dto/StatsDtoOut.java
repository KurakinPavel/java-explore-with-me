package ru.practicum.statsserverdto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatsDtoOut {
    protected String app;
    protected String uri;
    protected int hits;
}
