package ru.practicum.ewmserver.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class NewCompilationDto {
    protected List<Integer> events;
    protected Boolean pinned;
    @NotBlank
    @Size(min = 1, max = 50)
    protected String title;
}
