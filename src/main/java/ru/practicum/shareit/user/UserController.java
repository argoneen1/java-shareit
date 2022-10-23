package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserGetDto;
import ru.practicum.shareit.user.dto.UserInsertDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utils.Exceptions.getNoSuchElementException;

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
    public UserGetDto create(@RequestBody UserInsertDto element) {
        return UserMapper.toUserDto(service.create(element));
    }

    @PatchMapping("/{id}")
    public UserGetDto update(@RequestBody UserInsertDto element,
                             @PathVariable Long id) {
        element.setId(id);
        return UserMapper.toUserDto(service.update(element));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public UserGetDto findById(@PathVariable Long id) {
        return UserMapper.toUserDto(
                service.findById(id).orElseThrow(() -> getNoSuchElementException("user", id)));
    }

    @GetMapping
    public List<UserGetDto> findAll() {
        return service.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

}
