package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.validation.EndAfterStartValidator;
import ru.practicum.shareit.validation.NullOrNotBlankValidator;

import java.time.LocalDateTime;
import java.util.List;

public class ValidationTest {

    public static final List<LocalDateTime> timestamps = List.of(
            LocalDateTime.now().minusHours(2),
            LocalDateTime.now().minusHours(1),
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusHours(2)
    );

    List<BookItemRequestDto> bookingCreateDtos = List.of(
            new BookItemRequestDto(1L,
                    timestamps.get(0),
                    timestamps.get(1)
            ),
            new BookItemRequestDto(2L,
                    timestamps.get(3),
                    timestamps.get(1)
            ),
            new BookItemRequestDto(
                    3L,
                    timestamps.get(3),
                    timestamps.get(4)
            )
    );

    @Test
    void endAfterStartValidationTest() {
        EndAfterStartValidator validator = new EndAfterStartValidator();
        Assertions.assertTrue(validator.isValid(bookingCreateDtos.get(0), null));
        Assertions.assertFalse(validator.isValid(bookingCreateDtos.get(1), null));
    }

    @Test
    void nullOrNotBlankValidationTest() {
        NullOrNotBlankValidator validator = new NullOrNotBlankValidator();
        Assertions.assertTrue(validator.isValid(null, null));
        Assertions.assertTrue(validator.isValid("null", null));
        Assertions.assertFalse(validator.isValid("", null));
        Assertions.assertFalse(validator.isValid("   ", null));

    }
}
