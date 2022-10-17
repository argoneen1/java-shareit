package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class Item {
    private Long id;
    private String name;
    private Long owner;
    private String description;
    private Status status;
    private ItemRequest request;

    public Item(
            Long id,
            String name,
            Long owner,
            String description,
            Status status
    ) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.status = status;
    }

    //private final List<Booking> bookings = new ArrayList<>();
    public enum Status {
        RENTED,
        AVAILABLE
    }
}
