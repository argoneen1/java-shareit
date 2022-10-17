package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.utils.validation.NullOrNotBlank;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ItemUpdateDto {
    @NotNull
    private Long id;
    @NullOrNotBlank
    private String name;
    @NullOrNotBlank
    private String description;
    private Boolean available;
    @NotNull
    private Long owner;
}
