package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface ItemService {
    ItemGetDto create(@Valid ItemCreateDto element);

    ItemGetDto update(@Valid ItemUpdateDto element);

    boolean delete(Long id);

    Optional<ItemGetDto> get(Long id);

    List<ItemGetDto> getAll(Long sharerId);

    List<ItemGetDto> search(String text);
}
