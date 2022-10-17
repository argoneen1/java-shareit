package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.exceptions.OwnerIdNotMatches;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class ItemServiceImpl implements ItemService {
    private final ItemStorage storage;
    private final UserService userService;

    public ItemServiceImpl(ItemStorage storage, UserService userService) {
        this.storage = storage;
        this.userService = userService;
    }

    public ItemGetDto create(@Valid ItemCreateDto element) {
        if (userService.get(element.getOwner()).isEmpty()) {
            throw new NoSuchElementException("there is no such item owner with id " + element.getOwner());
        }
        return ItemMapper.toItemGetDto(storage.create(ItemMapper.toItem(element)));
    }

    public ItemGetDto update(@Valid ItemUpdateDto element) {
        Long elementId = element.getId();
        Long updatingItemOwnerId = element.getOwner();
        if (userService.get(updatingItemOwnerId).isEmpty()) {
            throw new NoSuchElementException("there is no such item owner with id " + updatingItemOwnerId);
        }
        Item updatingItem = storage.get(elementId)
                .orElseThrow(() -> new NoSuchElementException("there is no such item with id " + element.getId()));
        Long itemOwnerId = updatingItem.getOwner();
        if (!updatingItemOwnerId.equals(itemOwnerId)) {
            throw new OwnerIdNotMatches(updatingItemOwnerId, itemOwnerId);
        }
        return ItemMapper.toItemGetDto(storage.update(ItemMapper.toItem(element)));
    }

    public boolean delete(Long id) {
        return storage.delete(id);
    }

    public Optional<ItemGetDto> get(Long id) {
        return Optional.of(ItemMapper.toItemGetDto(
                storage.get(id)
                        .orElseThrow(() -> new NoSuchElementException("there is no such item with id " + id))
        ));
    }

    public List<ItemGetDto> getAll(Long sharerId) {
        return storage.get().stream()
                .filter(a -> a.getOwner().equals(sharerId))
                .map(ItemMapper::toItemGetDto)
                .collect(Collectors.toList());
    }

    public List<ItemGetDto> search(String text) {
        return storage.search(text).stream().map(ItemMapper::toItemGetDto).collect(Collectors.toList());
    }

}
