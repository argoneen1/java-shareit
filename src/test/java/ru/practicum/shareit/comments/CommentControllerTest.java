package ru.practicum.shareit.comments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.service.ItemService;

@WebMvcTest(controllers = ItemController.class)
public class CommentControllerTest {

    @MockBean
    ItemService service;

    @Autowired
    private MockMvc mvc;
}
