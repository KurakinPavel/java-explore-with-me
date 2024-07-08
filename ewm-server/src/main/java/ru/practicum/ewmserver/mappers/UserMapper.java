package ru.practicum.ewmserver.mappers;

import ru.practicum.ewmserver.dto.user.UserDto;
import ru.practicum.ewmserver.dto.user.UserDtoWithRating;
import ru.practicum.ewmserver.dto.user.UserShortDto;
import ru.practicum.ewmserver.dto.user.UserShotDtoWithRating;
import ru.practicum.ewmserver.model.User;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static UserDtoWithRating toUserDtoWithRating(User user) {
        return new UserDtoWithRating(
                user.getId(),
                user.getName(),
                user.getName(),
                user.getRating()
        );
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }

    public static UserShotDtoWithRating toUserShortDtoWithRating(User user) {
        return new UserShotDtoWithRating(
                user.getId(),
                user.getName(),
                user.getRating()
        );
    }

    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getId() != null ? userDto.getId() : 0,
                userDto.getName() != null ? userDto.getName() : "",
                userDto.getEmail() != null ? (userDto.getEmail()) : "",
                0
        );
    }
}
