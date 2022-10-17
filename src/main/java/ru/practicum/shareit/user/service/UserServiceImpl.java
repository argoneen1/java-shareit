package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserStorage storage;

    public UserServiceImpl(UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public UserDto create(UserDto element) throws CloneNotSupportedException {
        return UserMapper.toUserDto(storage.create(UserMapper.toUser(element)));
    }

    @Override
    public UserDto update(UserDto element) throws CloneNotSupportedException {
        return UserMapper.toUserDto(storage.update(UserMapper.toUser(element)));
    }

    @Override
    public boolean delete(Long id) {
        return storage.delete(id);
    }

    @Override
    public UserDto get(Long id) {
        return UserMapper.toUserDto(
                storage.get(id).orElseThrow(
                        () -> new NoSuchElementException("there is no such user with id " + id)
                )
        );
    }

    @Override
    public List<UserDto> get() {
        return storage.get().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}
