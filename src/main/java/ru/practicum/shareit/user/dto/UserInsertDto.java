package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.utils.validation.NullOrNotBlank;
import ru.practicum.shareit.utils.validation.ValidationMarker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
public class UserInsertDto {

    @Null(groups = ValidationMarker.onCreate.class)
    @NotNull(groups = ValidationMarker.onUpdate.class)
    private Long id;

    @NotBlank(groups = ValidationMarker.onCreate.class)
    @NullOrNotBlank(groups = ValidationMarker.onUpdate.class)
    private String name;

    @NotBlank(groups = ValidationMarker.onCreate.class)
    @NullOrNotBlank(groups = ValidationMarker.onUpdate.class)
    @Email(groups = {ValidationMarker.onCreate.class, ValidationMarker.onUpdate.class})
    private String email;
}
