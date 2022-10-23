package ru.practicum.shareit.booking.validation;

import ru.practicum.shareit.booking.dto.BookingCreateDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EndAfterStartValidator implements ConstraintValidator<EndAfterStart, BookingCreateDto> {

    @Override
    public boolean isValid(BookingCreateDto booking, ConstraintValidatorContext constraintValidatorContext) {
        return booking.getStart().isBefore(booking.getEnd());
    }
}
