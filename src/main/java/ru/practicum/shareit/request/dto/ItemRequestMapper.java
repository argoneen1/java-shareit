package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utils.Exceptions.getNoSuchElementException;

@RequiredArgsConstructor
@Component
public class ItemRequestMapper {

    private final UserService userService;

    public ItemRequestGetDto toGetDto(ItemRequest request) {
        return new ItemRequestGetDto(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                request.getItems().stream()
                        .map(ItemMapper::toSecondLevel)
                        .collect(Collectors.toList())
        );
    }

    public ItemRequest toItemRequest(ItemRequestCreateDto request) {
        return new ItemRequest(
                null,
                userService.findById(request.getRequesterId())
                        .orElseThrow(() -> getNoSuchElementException("user", request.getRequesterId())),
                request.getDescription(),
                LocalDateTime.now()
        );
    }
}
