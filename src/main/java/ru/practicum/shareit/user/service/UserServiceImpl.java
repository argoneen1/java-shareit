package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserGetDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class UserServiceImpl implements UserService {

    private final UserStorage storage;

    public UserServiceImpl(UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public UserGetDto create(@Valid UserCreateDto element) {
        return UserMapper.toUserDto(storage.create(UserMapper.toUser(element)));
    }

    @Override
    public UserGetDto update(@Valid UserUpdateDto element) {
        return UserMapper.toUserDto(storage.update(UserMapper.toUser(element)));
    }

    @Override
    public boolean delete(Long id) {
        return storage.delete(id);
    }

    @Override
    public Optional<UserGetDto> get(Long id) {
        Optional<User> element = storage.get(id);
        return element.isEmpty() ? Optional.empty() : Optional.of(UserMapper.toUserDto(element.get()));
    }

    @Override
    public List<UserGetDto> get() {
        return storage.get().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}
