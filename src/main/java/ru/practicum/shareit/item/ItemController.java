package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.utils.EndpointPaths;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utils.Constants.DEFAULT_PAGE_SIZE;
import static ru.practicum.shareit.utils.Constants.USER_HTTP_HEADER;
import static ru.practicum.shareit.utils.Exceptions.getNoSuchElementException;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@Validated
@RequestMapping(EndpointPaths.ITEM_ENDPOINT)
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;
    private final ItemMapper itemMapper;

    @PostMapping
    public ItemGetDto create(@RequestHeader(USER_HTTP_HEADER) Long sharerId,
                             @RequestBody ItemInsertDto element) {
        element.setOwner(sharerId);
        return itemMapper.toItemGetDto(service.create(element), sharerId);
    }

    @PatchMapping("/{id}")
    public ItemGetDto update(@RequestHeader(USER_HTTP_HEADER) Long sharerId,
                             @RequestBody ItemInsertDto element,
                             @PathVariable Long id) {
        element.setId(id);
        element.setOwner(sharerId);
        return itemMapper.toItemGetDto(service.update(element), sharerId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public ItemGetDto get(@RequestHeader(USER_HTTP_HEADER) Long userId,
                          @PathVariable Long id) {
        return itemMapper.toItemGetDto(
                service.findById(id)
                        .orElseThrow(() -> getNoSuchElementException("item", id)),
                userId);
    }

    @GetMapping
    public List<ItemGetDto> getAll(@RequestHeader(USER_HTTP_HEADER) Long sharerId,
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
        return service.findAllByOwnerId(sharerId, PageRequest.of( from / size, size)).stream()
                .map(a -> itemMapper.toItemGetDto(a, sharerId))
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemGetDto> search(@PathParam("text") String text,
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
        return text.isBlank() ?
                List.of() :
                service.search(text, PageRequest.of( from / size, size)).stream()
                        .map(a -> itemMapper.toItemGetDto(a, 0L))
                        .collect(Collectors.toList());
    }

    @PostMapping("/{itemId}/comment")
    public CommentGetDto postComment(@RequestHeader(USER_HTTP_HEADER) Long authorId,
                                     @PathVariable Long itemId,
                                     @RequestBody CommentInsertDto comment) {
        comment.setAuthorId(authorId);
        comment.setItemId(itemId);
        return CommentMapper.toGetDto(service.postComment(comment));
    }
}
