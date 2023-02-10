package ru.practicum.shareit;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.ValidationErrorResponse;
import ru.practicum.shareit.exceptions.Violation;
import ru.practicum.shareit.item.exceptions.OwnerIdNotMatches;
import ru.practicum.shareit.user.exceptions.UserAlreadyExistsException;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandlingControllerAdvices {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> noSuchElementExceptionHandler(final NoSuchElementException e) {
        return Map.of("not found", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> illegalArgumentExceptionHandler(final IllegalArgumentException e) {
        if (e.getMessage().startsWith("No enum constant"))
            return Map.of("error", "Unknown state: UNSUPPORTED_STATUS");
        return Map.of("bad request", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> userAlreadyExistsExceptionHandler(final UserAlreadyExistsException e) {
        return Map.of("such user already exists", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> userWithThisEmailAlreadyExists(final DataIntegrityViolationException e) {
        return Map.of("user with this email already exists", e.getMessage() == null ? "" : e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> ownerIdNotMatchesHandler(final OwnerIdNotMatches e) {
        return Map.of("not found", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }
}
