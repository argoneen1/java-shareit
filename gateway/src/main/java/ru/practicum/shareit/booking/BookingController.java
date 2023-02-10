package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.Constants.USER_ID_HTTP_HEADER;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;


	@GetMapping
	public ResponseEntity<Object>
	getBookings(@RequestHeader(USER_ID_HTTP_HEADER) long userId,
				@RequestParam(name = "state", defaultValue = "all") String stateParam,
				@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
				@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object>
	getBookingsByItemOwner(@RequestHeader(USER_ID_HTTP_HEADER) long userId,
						   @RequestParam(name = "state", defaultValue = "all") String stateParam,
						   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
						   @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking by item owner with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookingsByItemOwner(userId, state, from, size);
	}

	@PostMapping
	public ResponseEntity<Object> bookItem(@RequestHeader(USER_ID_HTTP_HEADER) long userId,
										   @RequestBody @Valid BookItemRequestDto requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.bookItem(userId, requestDto);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader(USER_ID_HTTP_HEADER) long userId,
											 @PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> confirm(@RequestHeader(USER_ID_HTTP_HEADER) long userId,
										  @PathVariable Long bookingId,
										  @RequestParam("approved") Boolean isApproved) {
		log.info("Confirm booking {}, userId={}", bookingId, userId);
		return bookingClient.confirm(userId, bookingId, isApproved);
	}
}
