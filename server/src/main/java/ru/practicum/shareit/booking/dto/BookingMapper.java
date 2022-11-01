package ru.practicum.shareit.booking.dto;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

@Service
public class BookingMapper {
    private final ItemService itemService;
    private final UserService userService;

    public BookingMapper(@Lazy ItemService itemService, @Lazy UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    public static BookingGetDto toGetDto(Booking booking) {
        return booking == null ? null :
                new BookingGetDto(
                        booking.getId(),
                        booking.getStart(),
                        booking.getEnd(),
                        ItemMapper.toSecondLevel(booking.getItem()),
                        UserMapper.toUserDto(booking.getBooker()),
                        booking.getStatus()
                );
    }

    public static BookingSecondLevelDto toSecondLevel(Booking booking) {
        return booking == null ? null :
                new BookingSecondLevelDto(
                        booking.getId(),
                        booking.getStart(),
                        booking.getEnd(),
                        booking.getBooker() == null ? null : booking.getBooker().getId(),
                        booking.getStatus()
                );
    }

    public Booking toBooking(BookingCreateDto dto) {
        return dto == null ? null :
                new Booking(null,
                        dto.getStart(),
                        dto.getEnd(),
                        itemService.findById(dto.getItemId()).orElse(null),
                        userService.findById(dto.getBooker()).orElse(null),
                        Status.WAITING);
    }
}
