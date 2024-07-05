package ru.practicum.ewmserver.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewmserver.dto.event.EventShortDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CompilationDto {
    protected Integer id;
    protected List<EventShortDto> events;
    protected Boolean pinned;
    protected String title;
}
