package ru.practicum.shareit.item.dto;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingSecondLevelDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.Status;
import ru.practicum.shareit.user.service.UserService;

import java.util.stream.Collectors;

@Component
public class ItemMapper {

    private final UserService userService;
    private final BookingRepository bookingRepository;

    public ItemMapper(@Lazy UserService userService, @Lazy BookingRepository bookingRepository) {
        this.userService = userService;
        this.bookingRepository = bookingRepository;
    }

    public ItemGetDto toItemGetDto(Item item, Long userId) {
        BookingSecondLevelDto lastBooking = null;
        BookingSecondLevelDto nextBooking = null;
        if (item.getOwner().getId().equals(userId)) {
            lastBooking = BookingMapper.toSecondLevel(bookingRepository.findLast(item.getId()).orElse(null));
            nextBooking = BookingMapper.toSecondLevel(bookingRepository.findNext(item.getId()).orElse(null));
        }
        return new ItemGetDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getStatus() == Status.AVAILABLE,
                lastBooking,
                nextBooking,
                item.getComments().stream().map(CommentMapper::toGetDto).collect(Collectors.toList())
        );

    }

    public static ItemSecondLevelDto toSecondLevel(Item item) {
        return new ItemSecondLevelDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getStatus() == Status.AVAILABLE);
    }

    public Item toItem(ItemInsertDto item) {
        return new Item(
                item.getId(),
                item.getName(),
                userService.findById(item.getOwner()).orElse(null),
                item.getDescription(),
                item.getAvailable() == null ? null :
                        item.getAvailable() ? Status.AVAILABLE : Status.RENTED);
    }
}
