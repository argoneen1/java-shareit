package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserGetDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequestMapping(path = "/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public UserGetDto create(@RequestBody UserCreateDto element) {
        return service.create(element);
    }

    @PatchMapping("/{id}")
    public UserGetDto update(@RequestBody UserUpdateDto element,
                             @PathVariable Long id) {
        element.setId(id);
        return service.update(element);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @GetMapping("/{id}")
    public UserGetDto get(@PathVariable Long id) {
        return service.get(id).orElseThrow(() -> new NoSuchElementException("there is no such user with id " + id));
    }

    @GetMapping
    public List<UserGetDto> get() {
        return service.get();
    }

}
