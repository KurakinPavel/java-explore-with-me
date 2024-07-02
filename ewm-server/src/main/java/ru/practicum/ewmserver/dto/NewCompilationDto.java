package ru.practicum.ewmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    protected Integer id;
    protected List<Integer> events;
    protected Boolean pinned;
    @Size(min = 1, max = 50)
    protected String title;
}
