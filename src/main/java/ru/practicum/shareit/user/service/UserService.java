package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserGetDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface UserService {
    UserGetDto create(@Valid UserCreateDto element);

    UserGetDto update(@Valid UserUpdateDto element);

    boolean delete(Long id);

    Optional<UserGetDto> get(Long id);

    List<UserGetDto> get();
}
