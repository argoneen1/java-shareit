package ru.practicum.shareit.booking.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingRequestsState;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static ru.practicum.shareit.utils.Exceptions.getNoSuchElementException;

@Service
@Validated
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingMapper bookingMapper;

    @Override
    public Booking create(@Valid BookingCreateDto booking) {
        validationOnCreate(booking);
        booking.setStatus(Status.WAITING);
        Long id = repository.save(bookingMapper.toBooking(booking)).getId();
        return repository.findById(id).orElseThrow(() -> getNoSuchElementException("booking", id));
    }

    private void validationOnCreate(BookingCreateDto booking) {
        Item item = itemService.findById(booking.getItemId())
                .orElseThrow(() -> getNoSuchElementException("item", booking.getItemId()));
        if (item.getStatus() != ru.practicum.shareit.item.model.Status.AVAILABLE) {
            throw new IllegalArgumentException("item is rented");
        }
        if (userService.findById(booking.getBooker()).isEmpty()) {
            throw getNoSuchElementException("user", booking.getBooker());
        }
        if (item.getOwner().getId().equals(booking.getBooker())) {
            throw new NoSuchElementException("owner trying to rent own item");
        }
    }

    @Override
    public Booking confirm(Long sharerId, Long bookingId, @NonNull Boolean isApproved) {
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> getNoSuchElementException("booking", bookingId));
        validationOnConfirm(sharerId, booking);
        booking.setStatus(isApproved ? Status.APPROVED : Status.REJECTED);
        repository.save(booking);
        return repository.findById(bookingId).orElseThrow(() -> getNoSuchElementException("booking", bookingId));
    }

    private void validationOnConfirm(Long sharerId, Booking booking) {
        Item item = itemService.findById(booking.getItem().getId())
                .orElseThrow(() -> getNoSuchElementException("user", sharerId));
        if (!item.getOwner().getId().equals(sharerId)) {
            throw new NoSuchElementException("got id (" + sharerId + ") and item owner id (" + item.getOwner() + ") doesn't matches");
        } else if (booking.getStatus() == Status.APPROVED || booking.getStatus() == Status.REJECTED) {
            throw new IllegalArgumentException("trying to change status after confirmation answer");
        }
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Booking> findByIdWithRequesterCheck(Long requesterId, Long bookingId) {
        Optional<Booking> booking = findById(bookingId);
        if (booking.isPresent()) {
            Booking bookingGet = booking.get();
            if (!(bookingGet.getBooker().getId().equals(requesterId) ||
                    bookingGet.getItemOwner().getId().equals(requesterId))) {
                throw getNoSuchElementException("user", requesterId);
            }
            return booking;
        }
        return Optional.empty();
    }

    @Override
    public List<Booking> findByBooker(Long sharerId, BookingRequestsState state, Pageable page) {
        if (userService.findById(sharerId).isEmpty()) {
            throw getNoSuchElementException("user", sharerId);
        }
        return repository.findByBookerIdAndState(sharerId,
                state,
                BookingRequestsState.ALL,// Другого способа вставить в аннотацию этот enum не нашёл, извините
                BookingRequestsState.PAST,
                BookingRequestsState.FUTURE,
                BookingRequestsState.CURRENT,
                BookingRequestsState.WAITING,
                BookingRequestsState.REJECTED,
                page).getContent();
    }

    @Override
    public List<Booking> findByOwner(Long sharerId, BookingRequestsState state, Pageable page) {
        if (userService.findById(sharerId).isEmpty()) {
            throw getNoSuchElementException("user", sharerId);
        }
        return repository.findByItemOwnerIdAndState(sharerId,
                state,
                BookingRequestsState.ALL,
                BookingRequestsState.PAST,
                BookingRequestsState.FUTURE,
                BookingRequestsState.CURRENT,
                BookingRequestsState.WAITING,
                BookingRequestsState.REJECTED,
                page).getContent();
    }

}
