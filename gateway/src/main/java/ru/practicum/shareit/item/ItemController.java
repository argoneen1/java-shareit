package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validation.ValidationMarker;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.websocket.server.PathParam;

import static ru.practicum.shareit.Constants.DEFAULT_PAGE_SIZE;
import static ru.practicum.shareit.Constants.USER_ID_HTTP_HEADER;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient client;

    @PostMapping
    @Validated(ValidationMarker.OnCreate.class)
    public ResponseEntity<Object> create(@RequestHeader(USER_ID_HTTP_HEADER) Long sharerId,
                                         @RequestBody ItemDto element) {
        return client.create(sharerId, element);
    }

    @PatchMapping("/{id}")
    @Validated(ValidationMarker.OnUpdate.class)
    public ResponseEntity<Object> update(@RequestHeader(USER_ID_HTTP_HEADER) Long sharerId,
                             @RequestBody ItemDto element,
                             @PathVariable Long id) {
        return client.update(sharerId, id, element);
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader(USER_ID_HTTP_HEADER) Long sharerId,
                       @PathVariable Long id) {
        client.delete(sharerId, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@RequestHeader(USER_ID_HTTP_HEADER) Long userId,
                          @PathVariable Long id) {
        return client.get(userId, id);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(USER_ID_HTTP_HEADER) Long sharerId,
                                   @RequestParam(value = "from",
                                           defaultValue = "0",
                                           required = false)
                                   @PositiveOrZero
                                   int from,

                                   @RequestParam(value = "size",
                                           defaultValue = DEFAULT_PAGE_SIZE,
                                           required = false)

                                   @Positive
                                   int size) {
        return client.getAll(sharerId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(USER_ID_HTTP_HEADER) Long sharerId,
                                         @PathParam("text") String text,
                                   @RequestParam(value = "from",
                                           defaultValue = "0",
                                           required = false)
                                   @PositiveOrZero
                                   int from,

                                   @RequestParam(value = "size",
                                           defaultValue = DEFAULT_PAGE_SIZE,
                                           required = false)
                                   @Positive
                                   int size) {
        return client.search(sharerId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(@RequestHeader(USER_ID_HTTP_HEADER) Long authorId,
                                     @PathVariable Long itemId,
                                     @RequestBody CommentDto comment) {
        return client.postComment(authorId, itemId, comment);
    }
}
