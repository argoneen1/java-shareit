package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentInsertDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.Status;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommentMapperTest {

    private UserService userService;
    private ItemService itemService;
    CommentMapper mapper;
    public static final List<LocalDateTime> timestamps = List.of(
            LocalDateTime.now().minusHours(2),
            LocalDateTime.now().minusHours(1),
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusHours(2)
    );
    List<User> users = List.of(
            new User(1L, "user1name", "user1@email.test"),
            new User(2L, "user2name", "user2@email.test"),
            new User(3L, "user3name", "user3@email.test")
    );
    List<Item> returnedItemsFromService = List.of(
            new Item(1L,
                    "item1name",
                    users.get(0),
                    "item1description",
                    null,
                    ru.practicum.shareit.item.model.Status.AVAILABLE),
            new Item(2L,
                    "питса",
                    users.get(1),
                    "вкусная питса \uD83C\uDF55 \uD83C\uDF55",
                    new ItemRequest(1L,
                            users.get(0),
                            "Хачу питсы",
                            timestamps.get(2)),
                    ru.practicum.shareit.item.model.Status.AVAILABLE),
            new Item(3L,
                    "item3name",
                    users.get(0),
                    "item3description",
                    null,
                    Status.RENTED)
    );

    CommentInsertDto insertDto = new CommentInsertDto(1L, 1L, "aboba");

    @BeforeEach
    void init() {
        userService = mock(UserService.class);
        itemService = mock(ItemService.class);
        mapper = new CommentMapper(userService, itemService);
    }

    @Test
    void toCommentCreateFailNoSuchItem() {
        when(itemService.findById(any()))
                .thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> mapper.toComment(insertDto));
    }

    @Test
    void toCommentCreateFailNoSuchUser() {
        when(itemService.findById(any()))
                .thenReturn(Optional.ofNullable(returnedItemsFromService.get(0)));
        when(userService.findById(any()))
                .thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> mapper.toComment(insertDto));
    }

    @Test
    void commentCreateSuccess() {
        when(itemService.findById(any()))
                .thenReturn(Optional.ofNullable(returnedItemsFromService.get(0)));
        when(userService.findById(any()))
                .thenReturn(Optional.ofNullable(users.get(0)));
        Comment mapped = mapper.toComment(insertDto);
        mapped.setId(1L);
        Assertions.assertEquals(new Comment(1L,
                "aboba",
                returnedItemsFromService.get(0),
                users.get(0),
                Instant.now()),
                mapped);
    }
}
