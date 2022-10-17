package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

@Data
@AllArgsConstructor
public class ItemGetDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;

    public ItemGetDto(
            Long id,
            String name,
            String description,
            Item.Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = status == Item.Status.AVAILABLE;
    }
}