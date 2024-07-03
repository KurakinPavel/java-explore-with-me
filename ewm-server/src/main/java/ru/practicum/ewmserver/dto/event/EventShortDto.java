package ru.practicum.ewmserver.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewmserver.dto.category.CategoryDto;
import ru.practicum.ewmserver.dto.user.UserShortDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventShortDto {
    protected Integer id;
    protected String annotation;
    protected CategoryDto category;
    protected Integer confirmedRequests;
    protected String eventDate;
    protected UserShortDto initiator;
    protected Boolean paid;
    protected String title;
    protected int views;
}
