package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@AllArgsConstructor
public class BookingCreateDto {

    private Long itemId;

    private Long booker;

    private LocalDateTime start;

    private LocalDateTime end;

    private Status status;
}
