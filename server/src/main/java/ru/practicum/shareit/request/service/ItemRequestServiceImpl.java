package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.utils.Exceptions.getNoSuchElementException;

@RequiredArgsConstructor
@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository repository;
    private final UserService userService;

    private final ItemRequestMapper itemRequestMapper;

    @Override
    public ItemRequest create(ItemRequestCreateDto element) {
        return repository.save(itemRequestMapper.toItemRequest(element));
    }

    @Override
    public List<ItemRequest> findAllByRequesterId(Long requesterId, Pageable page) {
        if (userService.findById(requesterId).isEmpty()) {
            return List.of();
        }
        return repository.findAllByRequesterId(requesterId, page).getContent();
    }

    @Override
    public List<ItemRequest> findAllExceptRequester(Long requesterId, Pageable page) {
        if (userService.findById(requesterId).isEmpty()) {
            return List.of();
        }
        return repository.findAllByRequesterIdIsNot(requesterId, page).getContent();
    }

    @Override
    public Optional<ItemRequest> findById(Long requesterId, Long itemRequestId) {
        if (userService.findById(requesterId).isEmpty()) {
            throw getNoSuchElementException("user", requesterId);
        }
        return findById(itemRequestId);
    }

    @Override
    public Optional<ItemRequest> findById(Long itemRequestId) {
        return repository.findById(itemRequestId);
    }
}
