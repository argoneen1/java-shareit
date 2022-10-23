package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingRequestsState;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    Booking create(@Valid BookingCreateDto booking);

    Booking confirm(Long sharerId, Long bookingId, Boolean isApproved);

    Optional<Booking> findById(Long requesterId, Long bookingId);

    List<Booking> findByBooker(Long sharerId, BookingRequestsState state, Integer from, Integer size);

    List<Booking> findByOwner(Long sharerId, BookingRequestsState state, Integer from, Integer size);
}
