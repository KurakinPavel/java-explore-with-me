package ru.practicum.ewmserver.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewmserver.dto.category.CategoryDto;
import ru.practicum.ewmserver.dto.user.UserShotDtoWithRating;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventShotDtoWithRating {
    protected Integer id;
    protected String annotation;
    protected CategoryDto category;
    protected Integer confirmedRequests;
    protected String eventDate;
    protected UserShotDtoWithRating initiator;
    protected Boolean paid;
    protected String title;
    protected int views;
    protected int rating;
}
