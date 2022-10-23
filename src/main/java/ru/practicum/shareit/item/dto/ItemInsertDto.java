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

    @Null(groups = ValidationMarker.OnCreate.class)
    @NotNull(groups = ValidationMarker.OnUpdate.class)
    private Long id;

    @NotBlank(groups = ValidationMarker.OnCreate.class)
    @NullOrNotBlank(groups = ValidationMarker.OnUpdate.class)
    private String name;

    @NotBlank(groups = ValidationMarker.OnCreate.class)
    @NullOrNotBlank(groups = ValidationMarker.OnUpdate.class)
    private String description;

    @NotNull(groups = ValidationMarker.OnCreate.class)
    private Boolean available;

    @NotNull(groups = {ValidationMarker.OnCreate.class, ValidationMarker.OnUpdate.class})
    private Long owner;
}
