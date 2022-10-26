package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserInsertDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.validation.ValidationMarker;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.utils.Exceptions.getNoSuchElementException;

@Service
@Validated
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    @Validated(ValidationMarker.OnCreate.class)
    public User create(@Valid UserInsertDto element) {
        return repository.save(UserMapper.toUser(element));
    }

    @Override
    @Validated(ValidationMarker.OnUpdate.class)
    public User update(@Valid UserInsertDto element) {
        User updated = repository.findById(element.getId())
                .orElseThrow(() -> getNoSuchElementException("user", element.getId()));
        return repository.save(fillFieldsForUpdate(element, updated));
    }

    private User fillFieldsForUpdate(UserInsertDto element, User updated) {
        if (element.getEmail() == null) { element.setEmail(updated.getEmail()); }
        if (element.getName() == null) { element.setName(updated.getName()); }
        return UserMapper.toUser(element);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<User> findAll(Pageable page) {
        return repository.findAll(page).getContent();
    }
}
