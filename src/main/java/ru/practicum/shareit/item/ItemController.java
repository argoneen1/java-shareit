package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemCreateOrUpdateDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final String userHttpHeader = "X-Sharer-User-Id";
    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping
    public ItemGetDto create(@RequestHeader(userHttpHeader) Long sharerId,
                             @RequestBody ItemCreateOrUpdateDto element) {
        element.setOwner(sharerId);
        if (!validateOnCreate(element)) {
            System.out.println(element);
            throw new IllegalArgumentException("Illegal item fields");
        }
        return service.create(element);
    }

    @PatchMapping("/{id}")
    public ItemGetDto update(@RequestHeader(userHttpHeader) Long sharerId,
                             @RequestBody ItemCreateOrUpdateDto item,
                             @PathVariable Long id) {
        item.setId(id);
        item.setOwner(sharerId);
        if (!validateOnUpdate(item)) {
            throw new IllegalArgumentException("Illegal item fields");
        }
        return service.update(item);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @GetMapping("/{id}")
    public ItemGetDto get(@PathVariable Long id) {
        try {
            return service.get(id);
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

    private boolean validateOnCreate(ItemCreateOrUpdateDto element) {
        return element.getOwner() != null &&
                element.getAvailable() != null &&
                element.getName() != null && !element.getName().equals("") &&
                element.getDescription() != null && !element.getDescription().equals("");
    }

    private boolean validateOnUpdate(ItemCreateOrUpdateDto element) {
        return element.getId() != null && element.getOwner() != null &&
                (element.getName() == null || !element.getName().equals("")) &&
                (element.getDescription() == null || !element.getDescription().equals(""));
    }
}
