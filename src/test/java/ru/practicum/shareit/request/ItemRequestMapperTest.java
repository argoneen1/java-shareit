package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestGetDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.UserControllerTest;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemRequestMapperTest {
    UserService userService;
    ItemRequestMapper mapper;

    List<ItemRequestCreateDto> itemRequestCreateDtos = List.of(
            new ItemRequestCreateDto(1L, "itemRequest1description"),
            new ItemRequestCreateDto(2L, "itemRequest2description"),
            new ItemRequestCreateDto(3L, "itemRequest3description")
    );
    List<ItemRequest> itemRequests = List.of(
            new ItemRequest(1L,
                    UserControllerTest.returnedUsers.get(0),
                    "itemRequest1description",
                    LocalDateTime.now()),
            new ItemRequest(2L,
                    UserControllerTest.returnedUsers.get(1),
                    "itemRequest2description",
                    LocalDateTime.now()),
            new ItemRequest(3L,
                    UserControllerTest.returnedUsers.get(2),
                    "itemRequest3description",
                    LocalDateTime.now())
    );
    List<ItemRequestGetDto> returnedItemRequest = List.of(
            new ItemRequestGetDto(1L,
                    "itemRequest1description",
                    itemRequests.get(0).getCreated(),
                    List.of()),
            new ItemRequestGetDto(2L,
                    "itemRequest1description",
                    itemRequests.get(1).getCreated(),
                    List.of()),
            new ItemRequestGetDto(3L,
                    "itemRequest1description",
                    itemRequests.get(2).getCreated(),
                    List.of())
    );

    @BeforeEach
    void init() {
        userService = mock(UserService.class);
        mapper = new ItemRequestMapper(userService);
    }

    @Test
    void toRequestFailNoSuchUser() {
        when(userService.findById(any()))
                .thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> mapper.toItemRequest(itemRequestCreateDtos.get(0)));
    }

    @Test
    void toRequest() {
        when(userService.findById(any()))
                .thenReturn(Optional.ofNullable(UserControllerTest.returnedUsers.get(0)));
        ItemRequest mapped = mapper.toItemRequest(itemRequestCreateDtos.get(0));
        mapped.setId(1L);
        Assertions.assertEquals(itemRequests.get(0), mapped);
    }

    @Test
    void toGetDto() {

    }
}
