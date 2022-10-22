package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingSecondLevelDto;
import ru.practicum.shareit.item.model.Status;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemGetDto {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    BookingSecondLevelDto lastBooking;
    BookingSecondLevelDto nextBooking;
    private List<CommentGetDto> comments;
}