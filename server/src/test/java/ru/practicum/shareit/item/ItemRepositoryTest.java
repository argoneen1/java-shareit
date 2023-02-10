package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.Status;
import ru.practicum.shareit.item.repositories.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    ItemRepository repository;
    @Autowired
    UserRepository userRepository;

    List<User> users = List.of(
            new User(1L, "user1name", "user1@email.test"),
            new User(2L, "user2name", "user2@email.test"),
            new User(3L, "user3name", "user3@email.test")
    );

    List<Item> itemsForSaving = List.of(
            new Item(null,
                    "item1name",
                    users.get(0),
                    "item1description",
                    null,
                    Status.AVAILABLE),
            new Item(null,
                    "питса",
                    users.get(1),
                    "вкусная питса \uD83C\uDF55 \uD83C\uDF55",
                    null,
                    Status.AVAILABLE),
            new Item(null,
                    "item3name",
                    users.get(0),
                    "item3description",
                    null,
                    Status.RENTED)
    );


    List<Item> returnedItems = List.of(
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
                    null,
                    Status.AVAILABLE),
            new Item(3L,
                    "item3name",
                    users.get(0),
                    "item3description",
                    null,
                    Status.RENTED)
    );

    @Test
    @Order(1)
    void searchByName() {
        if (userRepository.findAll().isEmpty()) {
            for (int i = 0; i < 3; i++) {

                userRepository.save(users.get(i));
                repository.save(itemsForSaving.get(i));
            }
        }
        Assertions.assertEquals(
                List.of(returnedItems.get(1)),
                repository.search("питса", PageRequest.of(0, 20)).getContent());
        Assertions.assertEquals(
                List.of(returnedItems.get(0)),
                repository.search("m1d", PageRequest.of(0, 20)).getContent());
    }

}
