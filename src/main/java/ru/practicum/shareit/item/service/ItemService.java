package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreateOrUpdateDto;
import ru.practicum.shareit.item.dto.ItemGetDto;

import java.util.List;

public interface ItemService {
    ItemGetDto create(ItemCreateOrUpdateDto element);

    ItemGetDto update(ItemCreateOrUpdateDto element);

    boolean delete(Long id);

    ItemGetDto get(Long id);

    List<ItemGetDto> getAll(Long sharerId);

    List<ItemGetDto> search(String text);
}
