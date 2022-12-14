package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class CommentInsertDto {

    @NotNull
    private Long authorId;

    @NotNull
    private Long itemId;

    @NotBlank
    private String text;
}
