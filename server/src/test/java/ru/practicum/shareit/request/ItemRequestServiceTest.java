package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemRequestServiceTest {
    ItemRequestService service;
    private ItemRequestRepository repository;
    private UserService userService;
    private ItemRequestMapper itemRequestMapper;

    @BeforeEach
    void init() {
        repository = mock(ItemRequestRepository.class);
        userService = mock(UserService.class);
        itemRequestMapper = mock(ItemRequestMapper.class);
        service = new ItemRequestServiceImpl(repository, userService, itemRequestMapper);
    }

    @Test
    void findAllByRequesterIdFailTest() {
        when(userService.findById(any()))
                .thenReturn(Optional.empty());
        Assertions.assertEquals(List.of(),
                service.findAllByRequesterId(1L, Pageable.unpaged()));
    }

    @Test
    void findAllByExceptRequesterIdFailTest() {
        when(userService.findById(any()))
                .thenReturn(Optional.empty());
        Assertions.assertEquals(List.of(),
                service.findAllExceptRequester(1L, Pageable.unpaged()));
    }

    @Test
    void findByIdFailTest() {
        when(userService.findById(any()))
                .thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> service.findById(1L, 1L));
    }
}
