package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingSecondLevelDto;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemGetDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private BookingSecondLevelDto lastBooking;
    private BookingSecondLevelDto nextBooking;
    private List<CommentGetDto> comments;
}