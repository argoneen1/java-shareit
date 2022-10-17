package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User create(User element);

    User update(User element);

    Optional<User> get(Long id);

    List<User> get();

    boolean delete(Long id);
}
