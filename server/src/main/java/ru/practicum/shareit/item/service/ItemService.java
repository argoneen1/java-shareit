package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.CommentInsertDto;
import ru.practicum.shareit.item.dto.ItemInsertDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Item create(ItemInsertDto element);

    Item update(ItemInsertDto element);

    void delete(Long id);

    Optional<Item> findById(Long id);

    List<Item> findAllByOwnerId(Long ownerId, Pageable page);

    List<Item> search(String text, Pageable page);

    Comment postComment(CommentInsertDto comment);

}
