package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingSecondLevelDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemInsertDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.Status;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemDtoTest {

    public static final List<LocalDateTime> timestamps = List.of(
            LocalDateTime.now().minusHours(2),
            LocalDateTime.now().minusHours(1),
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusHours(2)
    );
    ItemMapper itemMapper;
    UserService userService;
    ItemRequestService itemRequestService;
    BookingRepository bookingRepository;
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
                    ru.practicum.shareit.item.model.Status.AVAILABLE),
            new Item(2L,
                    "питса",
                    users.get(1),
                    "вкусная питса \uD83C\uDF55 \uD83C\uDF55",
                    new ItemRequest(1L,
                            users.get(0),
                            "Хачу питсы",
                            timestamps.get(2)),
                    ru.practicum.shareit.item.model.Status.AVAILABLE),
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

    List<BookingSecondLevelDto> bookingSecondLevelDtos = List.of(
            new BookingSecondLevelDto(1L,
                    timestamps.get(0),
                    timestamps.get(1),
                    1L,
                    ru.practicum.shareit.booking.model.Status.WAITING),
            new BookingSecondLevelDto(3L,
                    timestamps.get(3),
                    timestamps.get(4),
                    3L,
                    ru.practicum.shareit.booking.model.Status.REJECTED)
    );
    List<ItemGetDto> itemGetDtos = List.of(
            new ItemGetDto(1L,
                    returnedItemsFromService.get(0).getName(),
                    returnedItemsFromService.get(0).getDescription(),
                    returnedItemsFromService.get(0).getStatus() == Status.AVAILABLE,
                    null,
                    bookingSecondLevelDtos.get(0),
                    bookingSecondLevelDtos.get(1),
                    List.of()),
            new ItemGetDto(2L,
                    returnedItemsFromService.get(1).getName(),
                    returnedItemsFromService.get(1).getDescription(),
                    returnedItemsFromService.get(1).getStatus() == Status.AVAILABLE,
                    1L,
                    null,
                    null,
                    List.of())

    );

    @BeforeEach
    void init() {
        userService = mock(UserService.class);
        itemRequestService = mock(ItemRequestService.class);
        bookingRepository = mock(BookingRepository.class);
        itemMapper = new ItemMapper(userService, itemRequestService, bookingRepository);
    }

    @Test
    void toGetDtoWithBookings() {
        when(bookingRepository.findFirstByItemIdIsAndEndBeforeOrderByEndDesc(eq(1L), any()))
                .thenReturn(Optional.ofNullable(bookings.get(0)));
        when(bookingRepository.findFirstByItemIdIsAndStartAfterOrderByStartAsc(eq(1L), any()))
                .thenReturn(Optional.ofNullable(bookings.get(2)));
        Assertions.assertEquals(itemGetDtos.get(0), itemMapper.toItemGetDto(returnedItemsFromService.get(0), 1L));
    }

    @Test
    void toGetDtoWithoutBookings() {
        when(bookingRepository.findFirstByItemIdIsAndEndBeforeOrderByEndDesc(eq(2L), any()))
                .thenReturn(Optional.empty());
        when(bookingRepository.findFirstByItemIdIsAndStartAfterOrderByStartAsc(eq(2L), any()))
                .thenReturn(Optional.empty());
        Assertions.assertEquals(itemGetDtos.get(1), itemMapper.toItemGetDto(returnedItemsFromService.get(1), 2L));
    }

    @Test
    void toItem() {
        when(userService.findById(1L))
                .thenReturn(Optional.ofNullable(users.get(0)));
        when(itemRequestService.findById(any()))
                .thenReturn(Optional.empty());
        Assertions.assertEquals(returnedItemsFromService.get(0),
                itemMapper.toItem(new ItemInsertDto(1L,
                        returnedItemsFromService.get(0).getName(),
                        returnedItemsFromService.get(0).getDescription(),
                        returnedItemsFromService.get(0).getStatus() == Status.AVAILABLE,
                        returnedItemsFromService.get(0).getOwner().getId(),
                        null)));

    }
}
