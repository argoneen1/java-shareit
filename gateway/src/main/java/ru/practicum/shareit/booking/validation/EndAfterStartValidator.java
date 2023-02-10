package ru.practicum.shareit.booking.validation;

import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EndAfterStartValidator implements ConstraintValidator<EndAfterStart, BookItemRequestDto> {

    @Override
    public boolean isValid(BookItemRequestDto booking, ConstraintValidatorContext constraintValidatorContext) {
        return booking.getStart().isBefore(booking.getEnd());
    }
}
