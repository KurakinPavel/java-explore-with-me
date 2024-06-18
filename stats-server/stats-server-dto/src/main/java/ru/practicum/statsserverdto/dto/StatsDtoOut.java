package ru.practicum.statsserverdto.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatsDtoOut {
    protected String app;
    protected String uri;
    protected Long hits;

    public StatsDtoOut(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }

    public StatsDtoOut() {
    }
}
