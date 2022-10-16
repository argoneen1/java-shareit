package ru.practicum.shareit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ControllerAdvices {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> noSuchElementExceptionHandler(final NoSuchElementException e) {
        return Map.of("not found", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> illegalArgumentExceptionHandler(final IllegalArgumentException e) {
        return Map.of("bad request", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> cloneNotSupportedExceptionHandler(final CloneNotSupportedException e) {
        return Map.of("such user already exists", e.getMessage());
    }
}
