package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.validation.NullOrNotBlank;
import ru.practicum.shareit.validation.ValidationMarker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
public class UserDto {

    @Null(groups = {ValidationMarker.OnCreate.class, ValidationMarker.OnUpdate.class})
    private Long id;

    @NotBlank(groups = ValidationMarker.OnCreate.class)
    @NullOrNotBlank(groups = ValidationMarker.OnUpdate.class)
    private String name;

    @NotBlank(groups = ValidationMarker.OnCreate.class)
    @NullOrNotBlank(groups = ValidationMarker.OnUpdate.class)
    @Email(groups = {ValidationMarker.OnCreate.class, ValidationMarker.OnUpdate.class})
    private String email;
}
