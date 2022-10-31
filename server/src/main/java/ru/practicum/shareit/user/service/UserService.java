package ru.practicum.shareit.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.user.dto.UserInsertDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User create(UserInsertDto element);

    User update(UserInsertDto element);

    void delete(Long id);

    Optional<User> findById(Long id);

    List<User> findAll(Pageable page);
}
