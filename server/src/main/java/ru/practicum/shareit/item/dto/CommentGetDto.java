package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class CommentGetDto {
    Long id;
    String authorName;
    String text;
    Instant created;
}
