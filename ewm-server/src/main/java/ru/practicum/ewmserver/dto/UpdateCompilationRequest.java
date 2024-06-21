package ru.practicum.ewmserver.dto;

import javax.validation.constraints.Size;
import java.util.List;

public class UpdateCompilationRequest {
    protected List<Integer> events;
    protected Boolean pinned;
    @Size(min = 1, max = 50)
    protected String title;
}
