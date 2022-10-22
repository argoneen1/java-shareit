package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.Status;

import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utils.Exceptions.getNoSuchElementException;

@Repository
public class ItemStorageInMemory implements ItemStorage {
    private final Map<Long, Item> storage = new HashMap<>();
    private long idGenerator;

    @Override
    public Item create(Item element) {
        long id = ++idGenerator;
        element.setId(id);
        storage.put(id, element);
        return element;
    }

    @Override
    public Item update(Item element) {
        Long id = element.getId();
        if (!storage.containsKey(id)) {
            throw getNoSuchElementException("item", id);
        }
        Item updatedElement = storage.get(id);
        if (element.getName() == null) {
            element.setName(updatedElement.getName());
        }
        if (element.getDescription() == null) {
            element.setDescription(updatedElement.getDescription());
        }
        if (element.getStatus() == null) {
            element.setStatus(updatedElement.getStatus());
        }
        storage.put(element.getId(), element);
        return element;
    }

    @Override
    public Optional<Item> get(Long id) {
        Item item = storage.get(id);
        return item == null ? Optional.empty() : Optional.of(storage.get(id));
    }

    @Override
    public List<Item> get() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean delete(Long id) {
        return storage.remove(id) != null;
    }

    @Override
    public List<Item> search(String text) {
        return storage.values().stream()
                .filter(a -> a.getStatus() == Status.AVAILABLE &&
                        (a.getName().toLowerCase().contains(text.toLowerCase()) ||
                                a.getDescription().toLowerCase().contains(text.toLowerCase())))
                .collect(Collectors.toList());
    }
}
