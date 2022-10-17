package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.utils.validation.NullOrNotBlank;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UserUpdateDto {
    @NotNull
    private Long id;
    @NullOrNotBlank
    private String name;
    @NullOrNotBlank
    private String email;
}
