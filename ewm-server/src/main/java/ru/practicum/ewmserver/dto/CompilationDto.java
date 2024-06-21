package ru.practicum.ewmserver.dto;

import java.util.List;

public class CompilationDto {
    protected Integer id;
    protected List<EventShortDto> events;
    protected Boolean pinned;
    protected String title;
}
