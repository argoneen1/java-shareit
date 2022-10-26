package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.CommentInsertDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemInsertDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exceptions.OwnerIdNotMatches;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.Status;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.validation.ValidationMarker;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.utils.Exceptions.getNoSuchElementException;

@Service
@Validated
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @Override
    @Validated(ValidationMarker.OnCreate.class)
    public Item create(@Valid ItemInsertDto element) {
        if (userService.findById(element.getOwner()).isEmpty()) {
            throw getNoSuchElementException("item", element.getOwner());
        }
        return repository.save(itemMapper.toItem(element));
    }

    @Override
    @Validated(ValidationMarker.OnUpdate.class)
    public Item update(@Valid ItemInsertDto element) {
        Long elementId = element.getId();
        Long updatingItemOwnerId = element.getOwner();
        if (userService.findById(updatingItemOwnerId).isEmpty()) {
            throw getNoSuchElementException("item", updatingItemOwnerId);
        }
        Item updatingItem = repository.findById(elementId)
                .orElseThrow(() -> getNoSuchElementException("item", element.getId()));
        Long itemOwnerId = updatingItem.getOwner().getId();
        if (!updatingItemOwnerId.equals(itemOwnerId)) {
            throw new OwnerIdNotMatches(updatingItemOwnerId, itemOwnerId);
        }
        return repository.save(fillFieldsOnUpdate(element, updatingItem));
    }

    private Item fillFieldsOnUpdate(ItemInsertDto element, Item updatingItem) {
        if (element.getName() == null) {
            element.setName(updatingItem.getName());
        }
        if (element.getDescription() == null) {
            element.setDescription(updatingItem.getDescription());
        }
        if (element.getAvailable() == null) {
            element.setAvailable(updatingItem.getStatus() == Status.AVAILABLE);
        }
        return itemMapper.toItem(element);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Item> findAllByOwnerId(Long ownerId, Pageable page) {
        return repository.findAllByOwnerIdOrderByIdAsc(ownerId, page).getContent();
    }

    @Override
    public List<Item> search(String text, Pageable page) {
        return repository.search(text, page).getContent();
    }

    @Override
    public Comment postComment(CommentInsertDto comment) {
        if (findById(comment.getItemId())
                .orElseThrow(() -> getNoSuchElementException("item", comment.getItemId()))
                .getBookings().stream()
                .noneMatch(a -> a.getEnd().isBefore(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())))
        ) {
            throw new IllegalArgumentException("must have non-zero number of booking in past");
        }
        return commentRepository.save(commentMapper.toComment(comment));
    }
}
