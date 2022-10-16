package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User create(User element) throws CloneNotSupportedException;
    User update(User element) throws CloneNotSupportedException;
    Optional<User> get(Long id);
    List<User> get();
    boolean delete(Long id);
}
