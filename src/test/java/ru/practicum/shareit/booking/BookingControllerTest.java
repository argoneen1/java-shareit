package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemSecondLevelDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.Status;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.utils.Constants.USER_HTTP_HEADER;
import static ru.practicum.shareit.utils.EndpointPaths.BOOKING_ENDPOINT;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    public static final String TEST_ENDPOINT = BOOKING_ENDPOINT;
    public static final List<LocalDateTime> timestamps = List.of(
            LocalDateTime.now().minusHours(2),
            LocalDateTime.now().minusHours(1),
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusHours(2)
    );
    @MockBean
    BookingService service;
    @Autowired
    ObjectMapper mapper;
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
    List<Booking> bookings = List.of(
            new Booking(1L,
                    timestamps.get(0),
                    timestamps.get(1),
                    returnedItemsFromService.get(0),
                    users.get(0),
                    ru.practicum.shareit.booking.model.Status.WAITING),
            new Booking(2L,
                    timestamps.get(1),
                    timestamps.get(3),
                    returnedItemsFromService.get(0),
                    users.get(1),
                    ru.practicum.shareit.booking.model.Status.APPROVED),
            new Booking(3L,
                    timestamps.get(3),
                    timestamps.get(4),
                    returnedItemsFromService.get(0),
                    users.get(2),
                    ru.practicum.shareit.booking.model.Status.REJECTED)
    );
    List<ItemSecondLevelDto> itemSecondLevelDtos = List.of(
            new ItemSecondLevelDto(1L,
                    "item1name",
                    "item1description",
                    true,
                    null),
            new ItemSecondLevelDto(2L,
                    "item2name",
                    "item2description",
                    true,
                    5L),
            new ItemSecondLevelDto(3L,
                    "item3name",
                    "item3description",
                    true,
                    null)
    );
    List<BookingCreateDto> bookingCreateDtos = List.of(
            new BookingCreateDto(1L,
                    1L,
                    timestamps.get(0),
                    timestamps.get(1),
                    null),
            new BookingCreateDto(2L,
                    2L,
                    timestamps.get(1),
                    timestamps.get(3),
                    null),
            new BookingCreateDto(3L,
                    3L,
                    timestamps.get(3),
                    timestamps.get(4),
                    null)
    );
    List<BookingGetDto> getDtos = List.of(
            new BookingGetDto(1L,
                    timestamps.get(0),
                    timestamps.get(1),
                    itemSecondLevelDtos.get(0),
                    UserMapper.toUserDto(users.get(0)),
                    ru.practicum.shareit.booking.model.Status.WAITING),
            new BookingGetDto(2L,
                    timestamps.get(1),
                    timestamps.get(3),
                    itemSecondLevelDtos.get(0),
                    UserMapper.toUserDto(users.get(0)),
                    ru.practicum.shareit.booking.model.Status.WAITING),
            new BookingGetDto(3L,
                    timestamps.get(3),
                    timestamps.get(4),
                    itemSecondLevelDtos.get(0),
                    UserMapper.toUserDto(users.get(0)),
                    ru.practicum.shareit.booking.model.Status.WAITING)
    );
    @Autowired
    private MockMvc mvc;

    public <T> MockHttpServletRequestBuilder getStandardRequest(MockHttpServletRequestBuilder requestBuilders,
                                                                T mappedValue,
                                                                int userId) throws Exception {
        return
                requestBuilders
                        .content(mapper.writeValueAsString(mappedValue))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HTTP_HEADER, userId)
                        .accept(MediaType.APPLICATION_JSON);
    }

    @Test
    void postTest() throws Exception {
        when(service.create(any()))
                .thenReturn(bookings.get(0));
        mvc.perform(getStandardRequest(post(TEST_ENDPOINT), bookingCreateDtos.get(0), 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",
                        is(getDtos.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.booker.name",
                        is(getDtos.get(0).getBooker().getName())))
                .andExpect(jsonPath("$.start",
                        is(getDtos.get(0).getStart().toString())))
                .andExpect(jsonPath("$.end",
                        is(getDtos.get(0).getEnd().toString())))
                .andExpect(jsonPath("$.status",
                        is(getDtos.get(0).getStatus().toString())));
    }

    @Test
    void confirmTest() throws Exception {
        when(service.confirm(any(), any(), any()))
                .thenReturn(bookings.get(0));
        mvc.perform(getStandardRequest(patch(TEST_ENDPOINT + "/1/?approved=true"), null, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(getDtos.get(0).getId()), Long.class));

    }

    @Test
    void findByIdTest() {

    }

    @Test
    void findByBookerTest() {

    }

    @Test
    void findByOwner() {

    }
}
