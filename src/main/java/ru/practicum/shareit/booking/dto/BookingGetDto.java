package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemSecondLevelDto;
import ru.practicum.shareit.user.dto.UserGetDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingGetDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemSecondLevelDto item;
    private UserGetDto booker;
    private Status status;
}
