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

    public static Item toItem(ItemCreateOrUpdateDto item) {
        return new Item(
                item.getId(),
                item.getName(),
                item.getOwner(),
                item.getDescription(),
                item.getAvailable() == null ? null :
                        item.getAvailable() ? Item.Status.AVAILABLE : Item.Status.RENTED);
    }
}
