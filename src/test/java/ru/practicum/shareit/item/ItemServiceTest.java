package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentInsertDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemInsertDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.Status;
import ru.practicum.shareit.item.repositories.CommentRepository;
import ru.practicum.shareit.item.repositories.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemServiceTest {

    private final ItemInsertDto item1InsertDto =
            new ItemInsertDto(null, "item1name", "item1description",
                    true, 1L, null);
    ItemService service;
    UserService userService;
    ItemRepository repository;
    CommentRepository commentRepository;
    ItemMapper itemMapper;
    CommentMapper commentMapper;
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
                    Status.AVAILABLE),
            new Item(2L,
                    "питса",
                    users.get(1),
                    "вкусная питса \uD83C\uDF55 \uD83C\uDF55",
                    new ItemRequest(1L,
                            users.get(0),
                            "Хачу питсы",
                            LocalDateTime.ofInstant(Instant.now(),
                                    ZoneId.systemDefault())),
                    Status.AVAILABLE),
            new Item(3L,
                    "item3name",
                    users.get(0),
                    "item3description",
                    null,
                    Status.RENTED)
    );

    List<Booking> bookings = List.of(
            new Booking(1L,
                    LocalDateTime.now().minusHours(1),
                    LocalDateTime.now().minusMinutes(1),
                    returnedItemsFromService.get(0),
                    users.get(0), ru.practicum.shareit.booking.model.Status.WAITING)
    );


    @BeforeEach
    void beforeEach() {
        repository = mock(ItemRepository.class);
        commentRepository = mock(CommentRepository.class);
        userService = mock(UserService.class);
        itemMapper = mock(ItemMapper.class);
        commentMapper = mock(CommentMapper.class);
        service = new ItemServiceImpl(repository, commentRepository, userService, itemMapper, commentMapper);
    }

    @Test
    void postComment() {
        Item testedItem = new Item(1L,
                "item1name",
                users.get(0),
                "item1description",
                null,
                Status.AVAILABLE);
        testedItem.setBookings(bookings);
        when(repository.findById(any()))
                .thenReturn(Optional.of(testedItem));
        Comment comment = new Comment(1L,
                "comment1test",
                returnedItemsFromService.get(0),
                users.get(0),
                Instant.now());
        when(commentRepository.save(any()))
                .thenReturn(comment);
        CommentInsertDto insertDto = new CommentInsertDto(1L, 1L, "comment1test");
        Assertions.assertEquals(comment, service.postComment(insertDto));
    }

    @Test
    void postCommentNoBookingsError() {
        when(repository.findById(any()))
                .thenReturn(Optional.of(returnedItemsFromService.get(0)));
        Comment comment = new Comment(1L,
                "comment1test",
                returnedItemsFromService.get(0),
                users.get(0),
                Instant.now());
        when(commentRepository.save(any()))
                .thenReturn(comment);
        CommentInsertDto insertDto = new CommentInsertDto(1L, 1L, "comment1test");
        assertThrows(IllegalArgumentException.class, () -> service.postComment(insertDto));
    }

    @Test
    void createTest() {
        when(userService.findById(any()))
                .thenReturn(Optional.of(users.get(0)));
        when(repository.save(any()))
                .thenReturn(returnedItemsFromService.get(0));
        Assertions.assertEquals(returnedItemsFromService.get(0), service.create(item1InsertDto));
    }

    @Test
    void createFailNoUserTest() {
        when(userService.findById(any()))
                .thenReturn(Optional.empty());
        when(repository.save(any()))
                .thenReturn(returnedItemsFromService.get(0));
        assertThrows(NoSuchElementException.class, () -> service.create(item1InsertDto));
    }

    @Test
    void update1FieldTest() {
        ItemInsertDto updateItem = new ItemInsertDto(1L,
                "updateName",
                null,
                null,
                users.get(0).getId(),
                null);
        when(userService.findById(any()))
                .thenReturn(Optional.of(users.get(0)));
        when(repository.findById((any())))
                .thenReturn(Optional.of(returnedItemsFromService.get(0)));
        when(repository.save(any()))
                .thenReturn(returnedItemsFromService.get(0));
        Assertions.assertEquals(returnedItemsFromService.get(0), service.update(updateItem));
    }

    @Test
    void deleteTest() {

    }
}
