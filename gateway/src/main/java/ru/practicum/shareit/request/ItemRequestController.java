package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.Constants.DEFAULT_PAGE_SIZE;
import static ru.practicum.shareit.Constants.USER_ID_HTTP_HEADER;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_ID_HTTP_HEADER) Long sharerId,
                                         @Valid @RequestBody ItemRequestDto item) {
        return client.create(sharerId, item);
    }

    @GetMapping
    public ResponseEntity<Object>
    findAllByRequesterId(@RequestHeader(USER_ID_HTTP_HEADER) Long requesterId,
                         @RequestParam(value = "from", defaultValue = "0", required = false)
                         @PositiveOrZero
                         int from,
                         @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE, required = false)
                         @Positive
                         int size) {
        return client.getAllByRequesterId(requesterId, from, size);
    }

    @GetMapping("/all")
    public ResponseEntity<Object>
    findAll(@RequestHeader(USER_ID_HTTP_HEADER) Long requesterId,
            @RequestParam(value = "from", defaultValue = "0", required = false)
            @PositiveOrZero
            int from,
            @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE, required = false)
            @Positive
            int size) {

        return client.getAll(requesterId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@RequestHeader(USER_ID_HTTP_HEADER) Long requesterId,
                                      @PathVariable Long requestId) {
        return client.get(requesterId, requestId);
    }
}
