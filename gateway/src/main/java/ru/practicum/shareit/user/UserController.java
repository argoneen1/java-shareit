package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.ValidationMarker;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.Constants.DEFAULT_PAGE_SIZE;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient client;


    @PostMapping
    @Validated(ValidationMarker.OnCreate.class)
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto element) {
        System.out.println("user + " + element.toString());
        log.info("Create user with user={}", element);
        return client.create(element);
    }

    @PatchMapping("/{id}")
    @Validated(ValidationMarker.OnUpdate.class)
    public ResponseEntity<Object> update(@Valid @RequestBody UserDto element,
                                         @PathVariable Long id) {
        return client.update(id, element);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        client.delete(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        return client.get(id);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestParam(value = "from",
            defaultValue = "0",
            required = false)
                                          @PositiveOrZero
                                          int from,

                                          @RequestParam(value = "size",
                                                  defaultValue = DEFAULT_PAGE_SIZE,
                                                  required = false)
                                          @Positive
                                          int size) {
        return client.getAll(from, size);
    }

}
