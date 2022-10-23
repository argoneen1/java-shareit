package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestGetDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utils.Constants.USER_HTTP_HEADER;
import static ru.practicum.shareit.utils.Exceptions.getNoSuchElementException;

/**
 * TODO Sprint add-item-requests.
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService service;
    private final ItemRequestMapper itemRequestMapper;

    @PostMapping
    public ItemRequestGetDto create(@RequestHeader(USER_HTTP_HEADER) Long sharerId,
                                    @RequestBody ItemRequestCreateDto item) {
        item.setRequesterId(sharerId);
        return itemRequestMapper.toGetDto(service.create(item));
    }

    @GetMapping
    public List<ItemRequestGetDto> findAllByRequesterId(@RequestHeader(USER_HTTP_HEADER) Long requesterId) {
        return service.findAllByRequesterId(requesterId)
                .stream()
                .map(itemRequestMapper::toGetDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<ItemRequestGetDto> findAll(@RequestHeader(USER_HTTP_HEADER) Long requesterId,
                                           @RequestParam(value = "from", defaultValue = "0", required = false)
                                           @PositiveOrZero
                                           int from,
                                           @RequestParam(value = "size", defaultValue = "2147483646", required = false)
                                               @Positive
                                               int size) {
        return service.findAllPaging(requesterId, from / size, size)
                .stream()
                .map(itemRequestMapper::toGetDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{requestId}")
    public ItemRequestGetDto findById(@RequestHeader(USER_HTTP_HEADER) Long requesterId,
                                      @PathVariable Long requestId) {
        return itemRequestMapper.toGetDto(service
                .findById(requesterId, requestId)
                .orElseThrow(() -> getNoSuchElementException("item request", requestId)));
    }
}
