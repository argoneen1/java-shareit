package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemCreateOrUpdateDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Item create(Item element);
    Item update(Item element);
    Optional<Item> get(Long id);
    List<Item> get();
    boolean delete(Long id);
    List<Item> search(String text);
}
