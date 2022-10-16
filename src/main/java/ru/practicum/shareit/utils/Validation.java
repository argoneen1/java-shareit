package ru.practicum.shareit.utils;

import java.util.regex.Pattern;

public class Validation {
    public static boolean email(String email) {
        return Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@"
                        + "[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")
                .matcher(email)
                .matches();
    }
}
