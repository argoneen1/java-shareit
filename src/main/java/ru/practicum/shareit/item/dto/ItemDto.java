package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    private String name;
    private Long owner;
    private String description;
    private Boolean available;
    private Integer request;

    public ItemDto (String name,
                    String description,
                    Item.Status status,
                    Integer request) {
        this.name = name;
        this.description = description;
        this.available = status == Item.Status.AVAILABLE;
        this.request = request == null ? 0 : request;
    }
}
