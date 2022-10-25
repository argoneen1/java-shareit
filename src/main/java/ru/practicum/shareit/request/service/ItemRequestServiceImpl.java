package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.UserRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.utils.Exceptions.getNoSuchElementException;

@RequiredArgsConstructor
@Service
@Validated
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository repository;
    private final UserRepository userRepository;

    private final ItemRequestMapper itemRequestMapper;

    @Override
    public ItemRequest create(@Valid ItemRequestCreateDto element) {
        return repository.save(itemRequestMapper.toItemRequest(element));
    }

    @Override
    public List<ItemRequest> findAllByRequesterId(Long requesterId) {
        if (userRepository.findById(requesterId).isEmpty()) {
            throw getNoSuchElementException("user", requesterId);
        }
        return repository.findAllByRequesterId(requesterId);
    }

    @Override
    public List<ItemRequest> findAllPaging(Long requesterId, Pageable page) {
        return repository.findAllByNotRequesterIdWithPageable(
                        requesterId,
                        page)
                .getContent();
    }

    @Override
    public Optional<ItemRequest> findById(Long requesterId, Long itemRequestId) {
        if (userRepository.findById(requesterId).isEmpty()) {
            throw getNoSuchElementException("user", requesterId);
        }
        return findById(itemRequestId);
    }

    @Override
    public Optional<ItemRequest> findById(Long itemRequestId) {
        return repository.findById(itemRequestId);
    }
}
