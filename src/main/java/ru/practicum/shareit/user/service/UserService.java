package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    UserDto create(UserDto element) throws CloneNotSupportedException;
    UserDto update(UserDto element) throws CloneNotSupportedException;
    boolean delete(Long id);
    UserDto get(Long id);
    List<UserDto> get();
}
