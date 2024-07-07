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
public class EventFullDtoWithRating {
    protected Integer id;
    protected String annotation;
    protected CategoryDto category;
    protected Integer confirmedRequests;
    protected String createdOn;
    protected String description;
    protected String eventDate;
    protected UserShotDtoWithRating initiator;
    protected LocationDto location;
    protected Boolean paid;
    protected int participantLimit;
    protected String publishedOn;
    protected Boolean requestModeration;
    protected String state;
    protected String title;
    protected int views;
    protected int rating;
}
