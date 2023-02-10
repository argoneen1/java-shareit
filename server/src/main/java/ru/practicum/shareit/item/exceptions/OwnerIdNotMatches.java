package ru.practicum.shareit.item.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OwnerIdNotMatches extends IllegalArgumentException {
    long gotItemsOwnerID;
    long savedItemsOwnerId;

    @Override
    public String getMessage() {
        return "item`s owner id (" +
                savedItemsOwnerId +
                ") and update item`s owner id (" +
                gotItemsOwnerID +
                ") is not equal";
    }
}
