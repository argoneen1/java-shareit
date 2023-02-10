package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

import java.util.List;
import java.util.Optional;

public interface ItemRequestService {

    ItemRequest create(ItemRequestCreateDto element);

    List<ItemRequest> findAllByRequesterId(Long requesterId, Pageable page);

    List<ItemRequest> findAllExceptRequester(Long requesterId, Pageable page);

    Optional<ItemRequest> findById(Long requesterId, Long itemRequestId);

    Optional<ItemRequest> findById(Long itemRequestId);

}
