package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.Validation;

import java.util.List;

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
    public UserDto create(@RequestBody UserDto element) throws CloneNotSupportedException {

        if (!validateOnCreate(element)) {
            log.info("Illegal user fields");
            throw new IllegalArgumentException("Illegal user fields");
        }
        return service.create(element);
    }

    @PatchMapping("/{id}")
    public UserDto update(@RequestBody UserDto element, @PathVariable Long id) throws CloneNotSupportedException {
        element.setId(id);
        if (!validateOnUpdate(element)) {
            log.info("Illegal user fields");
            throw new IllegalArgumentException("Illegal user fields");
        }
        return service.update(element);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public List<UserDto> get() {
        return service.get();
    }

    private boolean validateOnCreate(UserDto element) {
        return element.getName() != null && !element.getName().equals("") &&
                element.getEmail() != null && !element.getEmail().equals("") && Validation.email(element.getEmail());
    }

    private boolean validateOnUpdate(UserDto element) {
        return (element.getName() == null || !element.getName().equals("")) &&
                (element.getEmail() == null || !element.getEmail().equals("") && Validation.email(element.getEmail()));
    }

}
