package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.user.dto.UserInsertDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    UserService service;
    UserRepository repository;

    List<UserInsertDto> createDtos = List.of(
            new UserInsertDto(null, "user1name", "user1@email.test"),
            new UserInsertDto(null, "user2name", "user2@email.test"),
            new UserInsertDto(null, "user3name", "user3@email.test")
    );
    List<UserInsertDto> updateDtos = List.of(
            new UserInsertDto(1L, "user1name", "user1@email.test"),
            new UserInsertDto(2L, "user2name", "user2@email.test"),
            new UserInsertDto(3L, "user3name", "user3@email.test")
    );
    List<User> returnedUsers = List.of(
            new User(1L, "user1name", "user1@email.test"),
            new User(2L, "user2name", "user2@email.test"),
            new User(3L, "user3name", "user3@email.test")
    );
    List<User> insertedUsers = List.of(
            new User(null, "user1name", "user1@email.test"),
            new User(null, "user2name", "user2@email.test"),
            new User(null, "user3name", "user3@email.test")
    );

    @BeforeEach
    void beforeEach() {
        repository = mock(UserRepository.class);
        service = new UserServiceImpl(repository);
    }

    @Test
    @Order(1)
    void getAllEmpty() {
        when(repository.findAll(PageRequest.of(0, 20)))
                .thenReturn(Page.empty());
        assertTrue(service.findAll(PageRequest.of(0, 20)).isEmpty());
    }

    @Test
    @Order(2)
    void getByIdError() {
        when(repository.findById(anyLong()))
                .thenThrow(new IllegalArgumentException("w"));
        assertThrows(IllegalArgumentException.class, () -> service.findById(99L));
    }

    @Test
    void create() {
        when(repository.save(any()))
                .thenReturn(returnedUsers.get(0));
        assertEquals(returnedUsers.get(0), service.create(createDtos.get(0)));
    }

    @Test
    void updateName() {
        User updateUser = new User(1L, "name", null);
        User returned = new User(1L, "name", "user1@email.test");
        when(repository.save(any()))
                .thenReturn(returned);
        when(repository.findById(any()))
                .thenReturn(Optional.of(returnedUsers.get(0)));
        assertEquals(returned, service.update(updateDtos.get(0)));
    }

    @Test
    void updateEmail() {
        User returned = new User(1L, "user1name", "user1Updated@email.test");
        when(repository.save(any()))
                .thenReturn(returned);
        when(repository.findById(any()))
                .thenReturn(Optional.of(returnedUsers.get(0)));
        assertEquals(returned, service.update(updateDtos.get(0)));
    }

    @Test
    void getById() {
        when(repository.findById(any()))
                .thenReturn(Optional.of(returnedUsers.get(0)));

    }
}
