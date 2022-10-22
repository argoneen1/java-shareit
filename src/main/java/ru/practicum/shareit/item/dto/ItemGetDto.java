package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingSecondLevelDto;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemGetDto {

    BookingSecondLevelDto lastBooking;
    BookingSecondLevelDto nextBooking;
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private List<CommentGetDto> comments;
}