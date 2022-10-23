package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentInsertDto;
import ru.practicum.shareit.item.dto.ItemInsertDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface ItemService {
    Item create(@Valid ItemInsertDto element);

    Item update(@Valid ItemInsertDto element);

    void delete(Long id);

    Optional<Item> findById(Long id);

    List<Item> findAllByOwnerId(Long ownerId, int from, int size);

    List<Item> search(String text, int from, int size);

    Comment postComment(CommentInsertDto comment);

}
