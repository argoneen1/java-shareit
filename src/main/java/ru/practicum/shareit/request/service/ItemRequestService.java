package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface ItemRequestService {

    ItemRequest create(@Valid ItemRequestCreateDto element);

    List<ItemRequest> findAllByRequesterId(Long RequesterId);

    List<ItemRequest> findAllPaging(Long requesterId, Pageable page);

    Optional<ItemRequest> findById(Long requesterId, Long itemRequestId);

    Optional<ItemRequest> findById(Long itemRequestId);

}
