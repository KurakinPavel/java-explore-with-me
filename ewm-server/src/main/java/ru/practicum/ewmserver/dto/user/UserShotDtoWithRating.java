package ru.practicum.ewmserver.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserShotDtoWithRating {
    protected int id;
    protected String name;
    protected int rating;
}
