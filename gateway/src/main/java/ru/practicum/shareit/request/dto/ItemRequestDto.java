package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ItemRequestDto {

    @NotNull
    private Long requesterId;

    @NotBlank
    private String description;
}
