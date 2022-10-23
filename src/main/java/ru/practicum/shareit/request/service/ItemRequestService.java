package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface ItemRequestService {

    ItemRequest create(@Valid ItemRequestCreateDto element);

    List<ItemRequest> findAllByRequesterId(Long RequesterId);

    List<ItemRequest> findAllPaging(Long requesterId, int from, int size);

    Optional<ItemRequest> findById(Long requesterId, Long itemRequestId);

    Optional<ItemRequest> findById(Long itemRequestId);

}
