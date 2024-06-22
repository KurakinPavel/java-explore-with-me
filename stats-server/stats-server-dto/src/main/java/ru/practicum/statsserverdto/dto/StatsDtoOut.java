package ru.practicum.statsserverdto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatsDtoOut {
    protected String app;
    protected String uri;
    protected Long hits;
}
