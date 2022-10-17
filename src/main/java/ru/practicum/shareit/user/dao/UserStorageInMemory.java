package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exceptions.UserAlreadyExistsException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserStorageInMemory implements UserStorage {
    private final Map<Long, User> storage = new HashMap<>();
    private long idGenerator;

    @Override
    public User create(User element) {
        if (storage.values().stream().anyMatch(a -> a.getEmail().equals(element.getEmail()))) {
            throw new UserAlreadyExistsException("there already is user with such email");
        }
        long id = ++idGenerator;
        element.setId(id);
        storage.put(id, element);
        return element;
    }

    @Override
    public User update(User element) {
        long id = element.getId();
        if (!storage.containsKey(id)) {
            throw new NoSuchElementException("there is no such user with id " + id);
        }
        if (storage.values().stream().anyMatch(a -> a.getEmail().equals(element.getEmail()))) {
            throw new UserAlreadyExistsException("there already is user with such email");
        }
        User updated = storage.get(id);
        if (element.getName() == null) {
            element.setName(updated.getName());
        }
        if (element.getEmail() == null) {
            element.setEmail(updated.getEmail());
        }
        storage.put(id, element);
        return element;
    }

    @Override
    public Optional<User> get(Long id) {
        User element = storage.get(id);
        return element == null ? Optional.empty() : Optional.of(element);
    }

    @Override
    public List<User> get() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean delete(Long id) {
        return storage.remove(id) != null;
    }
}
