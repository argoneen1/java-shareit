package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.utils.validation.NullOrNotBlank;
import ru.practicum.shareit.utils.validation.ValidationMarker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
public class ItemInsertDto {

    @Null(groups = ValidationMarker.onCreate.class)
    @NotNull(groups = ValidationMarker.onUpdate.class)
    private Long id;

    @NotBlank(groups = ValidationMarker.onCreate.class)
    @NullOrNotBlank(groups = ValidationMarker.onUpdate.class)
    private String name;

    @NotBlank(groups = ValidationMarker.onCreate.class)
    @NullOrNotBlank(groups = ValidationMarker.onUpdate.class)
    private String description;

    @NotNull(groups = ValidationMarker.onCreate.class)
    private Boolean available;

    @NotNull(groups = {ValidationMarker.onCreate.class, ValidationMarker.onUpdate.class})
    private Long owner;
}
