package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequestMapping("/items")
public class ItemController {
    private final String userHttpHeader = "X-Sharer-User-Id";
    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping
    public ItemGetDto create(@RequestHeader(userHttpHeader) Long sharerId,
                             @RequestBody ItemCreateDto element) {
        element.setOwner(sharerId);
        return service.create(element);
    }

    @PatchMapping("/{id}")
    public ItemGetDto update(@RequestHeader(userHttpHeader) Long sharerId,
                             @RequestBody ItemUpdateDto element,
                             @PathVariable Long id) {
        element.setId(id);
        element.setOwner(sharerId);
        return service.update(element);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @GetMapping("/{id}")
    public ItemGetDto get(@PathVariable Long id) {
        try {
            return service.get(id).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public List<ItemGetDto> getAll(@RequestHeader(userHttpHeader) Long sharerId) {
        return service.getAll(sharerId);
    }

    @GetMapping("/search")
    public List<ItemGetDto> search(@PathParam("text") String text) {
        return text.isBlank() ? List.of() : service.search(text);
    }


}
