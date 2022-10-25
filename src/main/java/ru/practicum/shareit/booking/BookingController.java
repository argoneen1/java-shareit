package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.BookingRequestsState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.utils.EndpointPaths;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utils.Constants.DEFAULT_PAGE_SIZE;
import static ru.practicum.shareit.utils.Constants.USER_HTTP_HEADER;
import static ru.practicum.shareit.utils.Exceptions.getNoSuchElementException;

@RestController
@Validated
@RequestMapping(path = EndpointPaths.BOOKING_ENDPOINT)
@Slf4j
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping
    public BookingGetDto post(@RequestHeader(USER_HTTP_HEADER) Long bookerId, // TODO: протестировать константа в application.properties -> @Value над константой -> аннотация
                              @RequestBody BookingCreateDto element) {
        element.setBooker(bookerId);
        return BookingMapper.toGetDto(service.create(element));
    }

    @PatchMapping("/{bookingId}")
    public BookingGetDto confirm(@RequestHeader(USER_HTTP_HEADER) Long sharerId,
                                 @PathVariable Long bookingId,
                                 @RequestParam("approved") Boolean isApproved) {
        return BookingMapper.toGetDto(service.confirm(sharerId, bookingId, isApproved));
    }

    @GetMapping("/{id}")
    public BookingGetDto findById(@RequestHeader(USER_HTTP_HEADER) Long sharerId,
                                  @PathVariable Long id) {
        return BookingMapper.toGetDto(service.findByIdWithRequesterCheck(sharerId, id)
                .orElseThrow(() -> getNoSuchElementException("booking", id)));
    }

    @GetMapping
    public List<BookingGetDto> findByBooker(@RequestHeader(USER_HTTP_HEADER)
                                            Long sharerId,

                                            @RequestParam(value = "state",
                                                    defaultValue = "ALL")
                                            BookingRequestsState state,

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

        return service.findByBooker(sharerId, state, PageRequest.of(from/ size, size) )
                .stream()
                .map(BookingMapper::toGetDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingGetDto> findByOwner(@RequestHeader(USER_HTTP_HEADER)
                                           Long sharerId,

                                           @RequestParam(value = "state",
                                                   defaultValue = "ALL")
                                           BookingRequestsState state,

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
        return service.findByOwner(sharerId, state, PageRequest.of(from/ size, size))
                .stream()
                .map(BookingMapper::toGetDto)
                .collect(Collectors.toList());
    }
}
