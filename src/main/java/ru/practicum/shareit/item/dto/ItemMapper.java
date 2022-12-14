package ru.practicum.shareit.item.dto;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingSecondLevelDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.Status;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class ItemMapper {

    private final UserService userService;
    private final ItemRequestService itemRequestService;
    private final BookingRepository bookingRepository;

    public ItemMapper(@Lazy UserService userService,
                      @Lazy ItemRequestService itemRequestService,
                      @Lazy BookingRepository bookingRepository) {
        this.userService = userService;
        this.itemRequestService = itemRequestService;
        this.bookingRepository = bookingRepository;
    }

    public static ItemSecondLevelDto toSecondLevel(Item item) {
        return new ItemSecondLevelDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getStatus() == Status.AVAILABLE,
                item.getRequest() == null ? null : item.getRequest().getId());
    }

    public ItemGetDto toItemGetDto(Item item, Long userId) {
        BookingSecondLevelDto lastBooking;
        BookingSecondLevelDto nextBooking;
        if (item.getOwner().getId().equals(userId)) {
            LocalDateTime now = LocalDateTime.now();
            lastBooking = BookingMapper.toSecondLevel(
                    bookingRepository.findFirstByItemIdIsAndEndBeforeOrderByEndDesc(item.getId(),
                            now).orElse(null));
            nextBooking = BookingMapper.toSecondLevel(
                    bookingRepository.findFirstByItemIdIsAndStartAfterOrderByStartAsc(item.getId(),
                            now).orElse(null));
        } else {
            lastBooking = null;
            nextBooking = null;
        }
        return new ItemGetDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getStatus() == Status.AVAILABLE,
                item.getRequest() == null ? null : item.getRequest().getId(),
                lastBooking,
                nextBooking,
                item.getComments().stream().map(CommentMapper::toGetDto).collect(Collectors.toList())
        );

    }

    public Item toItem(ItemInsertDto item) {
        return new Item(
                item.getId(),
                item.getName(),
                userService.findById(item.getOwner()).orElse(null),
                item.getDescription(),
                getRequest(item.getRequestId()),
                item.getAvailable() == null ? null :
                        item.getAvailable() ? Status.AVAILABLE : Status.RENTED);
    }

    private ItemRequest getRequest(Long requestId) {
        return requestId == null ? null :
                itemRequestService.findById(requestId).orElse(null);
    }
}
