package ru.practicum.shareit.utils;

import java.util.NoSuchElementException;

public class Exceptions {
    public static NoSuchElementException getNoSuchElementException(String elementName, Long id) {
        return new NoSuchElementException("there is no such " + elementName + " with id " + id);
    }
}
