package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static UserGetDto toUserDto(User user) {
        return new UserGetDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserCreateDto user) {
        return new User(
                0L,
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserUpdateDto user) {
        return new User(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
