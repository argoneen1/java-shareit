package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.dto.ItemCreateOrUpdateDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage storage;
    private final UserService userService;

    public ItemServiceImpl(ItemStorage storage, UserService userService) {
        this.storage = storage;
        this.userService = userService;
    }

    public ItemGetDto create(ItemCreateOrUpdateDto element) {
        try {
            userService.get(element.getOwner());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("there is no such item owner with id " +
                    e.getMessage().substring(e.getMessage().lastIndexOf(" ") + 1));
        }
        return ItemMapper.toItemGetDto(storage.create(ItemMapper.toItem(element)));
    }

    public ItemGetDto update(ItemCreateOrUpdateDto element) {
        Long updatingItemOwnerId = element.getOwner();
        try {
            userService.get(updatingItemOwnerId);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("there is no such item owner with id " +
                    e.getMessage().substring(e.getMessage().lastIndexOf(" ") + 1));
        }
        Long itemOwnerId = storage.get(element.getId()).get().getOwner();
        if (!updatingItemOwnerId.equals(itemOwnerId)) {
            throw new NoSuchElementException("item`s owner id (" +
                    itemOwnerId +
                    ") and update item`s owner id (" +
                    updatingItemOwnerId +
                    ") is not equal");
        }
        return ItemMapper.toItemGetDto(storage.update(ItemMapper.toItem(element)));
    }

    public boolean delete(Long id) {
        return storage.delete(id);
    }

    public ItemGetDto get(Long id) {
        Optional<Item> item = storage.get(id);
        if (item.isEmpty()) {
            throw new NoSuchElementException("there is no such item with id " + id);
        } else {
            return ItemMapper.toItemGetDto(item.get());
        }
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
