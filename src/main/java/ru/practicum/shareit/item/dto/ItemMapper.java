package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemGetDto toItemGetDto(Item item) {
        return new ItemGetDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getStatus() == Item.Status.AVAILABLE);
    }

    public static Item toItem(ItemUpdateDto item) {
        return new Item(
                item.getId(),
                item.getName(),
                item.getOwner(),
                item.getDescription(),
                item.getAvailable() == null ? null :
                        item.getAvailable() ? Item.Status.AVAILABLE : Item.Status.RENTED);
    }

    public static Item toItem(ItemCreateDto item) {
        return new Item(
                0L,
                item.getName(),
                item.getOwner(),
                item.getDescription(),
                item.getAvailable() == null ? null :
                        item.getAvailable() ? Item.Status.AVAILABLE : Item.Status.RENTED);
    }
}
