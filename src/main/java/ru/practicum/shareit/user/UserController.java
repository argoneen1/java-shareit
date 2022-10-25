package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserGetDto;
import ru.practicum.shareit.user.dto.UserInsertDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utils.Constants.DEFAULT_PAGE_SIZE;
import static ru.practicum.shareit.utils.EndpointPaths.USER_ENDPOINT;
import static ru.practicum.shareit.utils.Exceptions.getNoSuchElementException;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequestMapping(path = USER_ENDPOINT)
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
    public List<UserGetDto> findAll(@RequestParam(value = "from",
            defaultValue = "0",
            required = false)
                                        @PositiveOrZero
                                        int from,

                                    @RequestParam(value = "size",
                                            defaultValue = DEFAULT_PAGE_SIZE,
                                            required = false)
                                        @Positive
                                        int size) {
        return service.findAll(PageRequest.of(from / size, size)).stream()
                .map(UserMapper::toUserDto).collect(Collectors.toList());
    }

}
