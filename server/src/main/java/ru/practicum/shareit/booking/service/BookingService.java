package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingRequestsState;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    Booking create( BookingCreateDto booking);

    Booking confirm(Long sharerId, Long bookingId, Boolean isApproved);

    Optional<Booking> findByIdWithRequesterCheck(Long requesterId, Long bookingId);

    Optional<Booking> findById(Long id);

    List<Booking> findByBooker(Long sharerId, BookingRequestsState state, Pageable page);

    List<Booking> findByOwner(Long sharerId, BookingRequestsState state, Pageable page);
}
