package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.service.BookingService;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @MockBean
    BookingService service;

    @Autowired
    private MockMvc mvc;
}
