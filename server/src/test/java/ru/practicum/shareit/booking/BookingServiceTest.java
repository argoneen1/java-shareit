package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingRequestsState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.ItemSecondLevelDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.Status;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookingServiceTest {

    public static final List<LocalDateTime> timestamps = List.of(
            LocalDateTime.now().minusHours(2),
            LocalDateTime.now().minusHours(1),
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusHours(2)
    );
    BookingService service;
    List<User> users = List.of(
            new User(1L, "user1name", "user1@email.test"),
            new User(2L, "user2name", "user2@email.test"),
            new User(3L, "user3name", "user3@email.test")
    );
    List<Item> returnedItemsFromService = List.of(
            new Item(1L,
                    "item1name",
                    users.get(0),
                    "item1description",
                    null,
                    Status.AVAILABLE),
            new Item(2L,
                    "питса",
                    users.get(1),
                    "вкусная питса \uD83C\uDF55 \uD83C\uDF55",
                    new ItemRequest(1L,
                            users.get(0),
                            "Хачу питсы",
                            timestamps.get(2)),
                    Status.AVAILABLE),
            new Item(3L,
                    "item3name",
                    users.get(0),
                    "item3description",
                    null,
                    Status.RENTED)
    );
    List<Booking> bookings = List.of(
            new Booking(1L,
                    timestamps.get(0),
                    timestamps.get(1),
                    returnedItemsFromService.get(0),
                    users.get(0),
                    ru.practicum.shareit.booking.model.Status.WAITING),
            new Booking(2L,
                    timestamps.get(1),
                    timestamps.get(3),
                    returnedItemsFromService.get(0),
                    users.get(1),
                    ru.practicum.shareit.booking.model.Status.APPROVED),
            new Booking(3L,
                    timestamps.get(3),
                    timestamps.get(4),
                    returnedItemsFromService.get(0),
                    users.get(2),
                    ru.practicum.shareit.booking.model.Status.REJECTED)
    );
    List<ItemSecondLevelDto> itemSecondLevelDtos = List.of(
            new ItemSecondLevelDto(1L,
                    "item1name",
                    "item1description",
                    true,
                    null),
            new ItemSecondLevelDto(2L,
                    "item2name",
                    "item2description",
                    true,
                    5L),
            new ItemSecondLevelDto(3L,
                    "item3name",
                    "item3description",
                    true,
                    null)
    );
    List<BookingCreateDto> bookingCreateDtos = List.of(
            new BookingCreateDto(1L,
                    1L,
                    timestamps.get(0),
                    timestamps.get(1),
                    null),
            new BookingCreateDto(2L,
                    2L,
                    timestamps.get(1),
                    timestamps.get(3),
                    null),
            new BookingCreateDto(3L,
                    3L,
                    timestamps.get(3),
                    timestamps.get(4),
                    null)
    );

    private BookingRepository repository;
    private ItemService itemService;
    private UserService userService;
    private BookingMapper bookingMapper;

    @BeforeEach
    void init() {
        repository = mock(BookingRepository.class);
        itemService = mock(ItemService.class);
        userService = mock(UserService.class);
        bookingMapper = mock(BookingMapper.class);
        service = new BookingServiceImpl(repository, itemService, userService, bookingMapper);
    }

    @Test
    void createFailNoSuchItem() {
        when(itemService.findById(any()))
                .thenReturn(Optional.empty());
        when(userService.findById(any()))
                .thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.create(bookingCreateDtos.get(0)));
    }

    @Test
    void createFailItemUnavailable() {
        when(itemService.findById(any()))
                .thenReturn(Optional.of(returnedItemsFromService.get(2)));
        when(userService.findById(any()))
                .thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.create(bookingCreateDtos.get(0)));
    }

    @Test
    void createFailNoSuchUser() {
        when(itemService.findById(any()))
                .thenReturn(Optional.of(returnedItemsFromService.get(0)));
        when(userService.findById(any()))
                .thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.create(bookingCreateDtos.get(0)));
    }

    @Test
    void createFailRentOwnItem() {
        when(itemService.findById(any()))
                .thenReturn(Optional.of(returnedItemsFromService.get(0)));
        when(userService.findById(any()))
                .thenReturn(Optional.ofNullable(users.get(0)));
        assertThrows(NoSuchElementException.class, () -> service.create(bookingCreateDtos.get(0)));
    }

    @Test
    void createItemSuccess() {
        BookingCreateDto booker = new BookingCreateDto(1L,
                2L,
                timestamps.get(0),
                timestamps.get(1),
                null);
        when(itemService.findById(any()))
                .thenReturn(Optional.of(returnedItemsFromService.get(0)));
        when(userService.findById(any()))
                .thenReturn(Optional.ofNullable(users.get(1)));
        when(repository.save(any()))
                .thenReturn(bookings.get(0));
        when(repository.findById(any()))
                .thenReturn(Optional.ofNullable(bookings.get(0)));
        Assertions.assertEquals(bookings.get(0), service.create(booker));
    }

    @Test
    void confirmFailWhenNoSuchBooking() {
        when(repository.findById(any()))
                .thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.confirm(1L, 1L, true));
    }

    @Test
    void confirmFailWhenNoSuchItem() {
        when(repository.findById(any()))
                .thenReturn(Optional.ofNullable(bookings.get(0)));
        when(itemService.findById(any()))
                .thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.confirm(1L, 1L, true));
    }

    @Test
    void confirmFailWhenOwnerIdAndRequesterIdDoesntMatch() {
        when(repository.findById(any()))
                .thenReturn(Optional.ofNullable(bookings.get(0)));
        when(itemService.findById(any()))
                .thenReturn(Optional.of(returnedItemsFromService.get(1)));
        assertThrows(NoSuchElementException.class, () -> service.confirm(1L, 1L, true));
    }

    @Test
    void confirmFailWhenChangeSettledStatus() {
        when(repository.findById(any()))
                .thenReturn(Optional.ofNullable(bookings.get(1)));
        when(itemService.findById(any()))
                .thenReturn(Optional.of(returnedItemsFromService.get(1)));
        assertThrows(IllegalArgumentException.class, () -> service.confirm(2L, 2L, true));
    }

    @Test
    void confirm() {
        when(repository.findById(any()))
                .thenReturn(Optional.ofNullable(bookings.get(0)));
        when(itemService.findById(any()))
                .thenReturn(Optional.of(returnedItemsFromService.get(0)));
        Assertions.assertEquals(bookings.get(0), service.confirm(1L, 1L, true));
    }

    @Test
    void findByIdWithRequesterCheckFailRequesterAndOwnerOrBookerIdDoesntMatch() {
        when(repository.findById(any()))
                .thenReturn(Optional.ofNullable(bookings.get(1)));
        when(itemService.findById(any()))
                .thenReturn(Optional.of(returnedItemsFromService.get(0)));
        assertThrows(NoSuchElementException.class, () -> service.findByIdWithRequesterCheck(3L, 2L));
    }

    @Test
    void findByBookerFailNoSuchUser() {
        when(userService.findById(any()))
                .thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.findByBooker(3L,
                BookingRequestsState.ALL,
                Pageable.unpaged()));
    }

    @Test
    void findByOwnerFailNoSuchUser() {
        when(userService.findById(any()))
                .thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.findByOwner(3L,
                BookingRequestsState.ALL,
                Pageable.unpaged()));
    }

    @Test
    void findByOwner() {
        Page<Booking> page = new PageImpl<>(List.of(bookings.get(2)));

        when(userService.findById(any()))
                .thenReturn(Optional.ofNullable(users.get(2)));
        when(repository.findByItemOwnerIdAndState(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(page);
        Assertions.assertEquals(List.of(bookings.get(2)), service.findByOwner(3L, BookingRequestsState.ALL, Pageable.unpaged()));
    }

    @Test
    void findByBooker() {
        Page<Booking> page = new PageImpl<>(List.of(bookings.get(2)));
        when(userService.findById(any()))
                .thenReturn(Optional.ofNullable(users.get(2)));
        when(repository.findByBookerIdAndState(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(page);
        Assertions.assertEquals(List.of(bookings.get(2)), service.findByBooker(3L, BookingRequestsState.ALL, Pageable.unpaged()));
    }
}
