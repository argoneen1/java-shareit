package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.dto.CommentGetDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemInsertDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.Status;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.utils.Constants.USER_HTTP_HEADER;
import static ru.practicum.shareit.utils.EndpointPaths.ITEM_ENDPOINT;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
public class ItemControllerTest {

    public static final String TEST_ENDPOINT = ITEM_ENDPOINT;
    private final ItemInsertDto item1InsertDto =
            new ItemInsertDto(null, "item1name", "item1description",
                    true, 1L, null);
    private final ItemInsertDto item2InsertDto =
            new ItemInsertDto(null, "item2name", "item2description",
                    false, 2L, 1L);
    private final ItemInsertDto item3InsertDto =
            new ItemInsertDto(null, "item3name", "item3description",
                    true, 3L, null);
    @MockBean
    ItemService service;
    @MockBean
    ItemMapper itemMapper;
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
    List<ItemGetDto> returnedItemsFromController = List.of(
            new ItemGetDto(1L,
                    "item1name",
                    "item1description",
                    true,
                    null,
                    null,
                    null,
                    List.of(new CommentGetDto(1L, "comment1authorName", "comment1text", Instant.now()))
            ),
            new ItemGetDto(2L,
                    "питса",
                    "вкусная питса \uD83C\uDF55 \uD83C\uDF55",
                    true,
                    1L,
                    null,
                    null,
                    null),
            new ItemGetDto(3L,
                    "item3name",
                    "item3description",
                    false,
                    null,
                    null,
                    null,
                    null)
    );
    @MockBean
    private UserService userService;
    @MockBean
    private ItemRequestService itemRequestService;
    @MockBean
    private BookingRepository bookingRepository;
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setItemMapperSettings() {
        when(itemMapper.toItemGetDto(eq(returnedItemsFromService.get(0)), any()))
                .thenReturn(returnedItemsFromController.get(0));
        when(itemMapper.toItemGetDto(eq(returnedItemsFromService.get(1)), any()))
                .thenReturn(returnedItemsFromController.get(1));
        when(itemMapper.toItemGetDto(eq(returnedItemsFromService.get(2)), any()))
                .thenReturn(returnedItemsFromController.get(2));
    }

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
    @Order(1)
    void save1() throws Exception {
        when(service.create(any()))
                .thenReturn(returnedItemsFromService.get(0));
        mvc.perform(getStandardRequest(post(TEST_ENDPOINT), item1InsertDto, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",
                        is(returnedItemsFromController.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.name",
                        is(returnedItemsFromController.get(0).getName())))
                .andExpect(jsonPath("$.description",
                        is(returnedItemsFromController.get(0).getDescription())))
                .andExpect(jsonPath("$.requestId",
                        is(returnedItemsFromController.get(0).getRequestId()), Long.class))
                .andExpect(jsonPath("$.available",
                        is(returnedItemsFromController.get(0).getAvailable())));
    }

    @Test
    @Order(2)
    void save2() throws Exception {
        when(service.create(any()))
                .thenReturn(returnedItemsFromService.get(1));
        mvc.perform(getStandardRequest(post(TEST_ENDPOINT), item2InsertDto, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",
                        is(returnedItemsFromController.get(1).getId()), Long.class))
                .andExpect(jsonPath("$.name",
                        is(returnedItemsFromController.get(1).getName())))
                .andExpect(jsonPath("$.description",
                        is(returnedItemsFromController.get(1).getDescription())))
                .andExpect(jsonPath("$.requestId",
                        is(returnedItemsFromController.get(1).getRequestId()), Long.class))
                .andExpect(jsonPath("$.available",
                        is(returnedItemsFromController.get(1).getAvailable())));
    }

    @Test
    @Order(3)
    void save3() throws Exception {
        when(service.create(any()))
                .thenReturn(returnedItemsFromService.get(2));
        mvc.perform(getStandardRequest(post(TEST_ENDPOINT), item3InsertDto, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",
                        is(returnedItemsFromController.get(2).getId()), Long.class))
                .andExpect(jsonPath("$.name",
                        is(returnedItemsFromController.get(2).getName())))
                .andExpect(jsonPath("$.description",
                        is(returnedItemsFromController.get(2).getDescription())))
                .andExpect(jsonPath("$.requestId",
                        is(returnedItemsFromController.get(2).getRequestId()), Long.class))
                .andExpect(jsonPath("$.available",
                        is(returnedItemsFromController.get(2).getAvailable())));
    }


    @Test
    @Order(5)
    void getAll() throws Exception {
        when(service.findAllByOwnerId(any(), any()))
                .thenReturn(returnedItemsFromService);
        mvc.perform(getStandardRequest(get(TEST_ENDPOINT), null, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(returnedItemsFromController.get(0).getId()), Long.class))
                .andExpect(jsonPath("$[1].id", is(returnedItemsFromController.get(1).getId()), Long.class))
                .andExpect(jsonPath("$[2].id", is(returnedItemsFromController.get(2).getId()), Long.class));
    }

    @Test
    @Order(6)
    void getByIdError() throws Exception {
        mvc.perform(getStandardRequest(get(TEST_ENDPOINT + "/99"), null, 1))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(7)
    void getUser1() throws Exception {
        when(service.findById(1L))
                .thenReturn(Optional.of(returnedItemsFromService.get(0)));
        System.out.println(returnedItemsFromController.get(0).getId());
        mvc.perform(getStandardRequest(get(TEST_ENDPOINT + "/1"), null, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",
                        is(returnedItemsFromController.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.name",
                        is(returnedItemsFromController.get(0).getName())))
                .andExpect(jsonPath("$.description",
                        is(returnedItemsFromController.get(0).getDescription())))
                .andExpect(jsonPath("$.requestId",
                        is(returnedItemsFromController.get(0).getRequestId()), Long.class))
                .andExpect(jsonPath("$.available",
                        is(returnedItemsFromController.get(0).getAvailable())));
    }

    @Test
    @Order(8)
    void getUser2() throws Exception {
        when(service.findById(any()))
                .thenReturn(Optional.of(returnedItemsFromService.get(1)));
        mvc.perform(getStandardRequest(get(TEST_ENDPOINT + "/1"), null, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",
                        is(returnedItemsFromController.get(1).getId()), Long.class))
                .andExpect(jsonPath("$.name",
                        is(returnedItemsFromController.get(1).getName())))
                .andExpect(jsonPath("$.description",
                        is(returnedItemsFromController.get(1).getDescription())))
                .andExpect(jsonPath("$.requestId",
                        is(returnedItemsFromController.get(1).getRequestId()), Long.class))
                .andExpect(jsonPath("$.available",
                        is(returnedItemsFromController.get(1).getAvailable())));
    }

    @Test
    @Order(9)
    void updateUserName() throws Exception {
        Item returnedUser = new Item(1L,
                "item1nameUpdated",
                users.get(0),
                "item1description",
                null,
                Status.AVAILABLE);
        ItemGetDto insertedDto = new ItemGetDto(1L,
                "item1nameUpdated",
                "item1description",
                true,
                null,
                null,
                null,
                List.of(new CommentGetDto(1L, "comment1authorName", "comment1text", Instant.now()))
        );
        when(service.update(any()))
                .thenReturn(returnedUser);
        mvc.perform(getStandardRequest(patch(TEST_ENDPOINT + "/1"), insertedDto, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",
                        is(returnedItemsFromController.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.name",
                        is(returnedItemsFromController.get(0).getName())))
                .andExpect(jsonPath("$.description",
                        is(returnedItemsFromController.get(0).getDescription())))
                .andExpect(jsonPath("$.requestId",
                        is(returnedItemsFromController.get(0).getRequestId()), Long.class))
                .andExpect(jsonPath("$.available",
                        is(returnedItemsFromController.get(0).getAvailable())));
    }

    @Test
    @Order(10)
    void updateUserEmail() throws Exception {
        Item returnedUser = new Item(1L,
                "item1nameUpdated",
                users.get(0),
                "item1descriptionUpdated",
                null,
                Status.AVAILABLE);
        ItemGetDto insertedDto = new ItemGetDto(1L,
                "item1nameUpdated",
                "item1descriptionUpdated",
                true,
                null,
                null,
                null,
                List.of(new CommentGetDto(1L, "comment1authorName", "comment1text", Instant.now()))
        );
        when(service.update(any()))
                .thenReturn(returnedUser);
        mvc.perform(getStandardRequest(patch(TEST_ENDPOINT + "/1"), insertedDto, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",
                        is(returnedItemsFromController.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.name",
                        is(returnedItemsFromController.get(0).getName())))
                .andExpect(jsonPath("$.description",
                        is(returnedItemsFromController.get(0).getDescription())))
                .andExpect(jsonPath("$.requestId",
                        is(returnedItemsFromController.get(0).getRequestId()), Long.class))
                .andExpect(jsonPath("$.available",
                        is(returnedItemsFromController.get(0).getAvailable())));
    }

    @Test
    @Order(11)
    void updateUserAll() throws Exception {
        Item returnedUser = new Item(1L,
                "item1nameUpdated",
                users.get(0),
                "item1descriptionUpdated",
                null,
                Status.AVAILABLE);
        ItemGetDto insertedDto = new ItemGetDto(1L,
                "item1nameUpdated",
                "item1descriptionUpdated",
                true,
                null,
                null,
                null,
                List.of(new CommentGetDto(1L, "comment1authorName", "comment1text", Instant.now()))
        );
        when(service.update(any()))
                .thenReturn(returnedUser);
        mvc.perform(getStandardRequest(patch(TEST_ENDPOINT + "/1"), insertedDto, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",
                        is(returnedItemsFromController.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.name",
                        is(returnedItemsFromController.get(0).getName())))
                .andExpect(jsonPath("$.description",
                        is(returnedItemsFromController.get(0).getDescription())))
                .andExpect(jsonPath("$.requestId",
                        is(returnedItemsFromController.get(0).getRequestId()), Long.class))
                .andExpect(jsonPath("$.available",
                        is(returnedItemsFromController.get(0).getAvailable())));
    }

    @Test
    @Order(12)
    void userDelete() throws Exception {

        mvc.perform(getStandardRequest(delete(TEST_ENDPOINT + "/1"), null, 1))
                .andExpect(status().isOk());
        mvc.perform(getStandardRequest(get(TEST_ENDPOINT + "/1"), null, 1))
                .andExpect(status().isNotFound());
        verify(service, times(1)).delete(any());
    }

    @Test
    @Order(13)
    void userDeleteError() throws Exception {
        mvc.perform(getStandardRequest(delete(TEST_ENDPOINT + "/99"), null, 1))
                .andExpect(status().isOk());
        verify(service, times(1)).delete(any());
    }

    @Test
    @Order(14)
    void userCreateError() throws Exception {
        when(service.create(any()))
                .thenThrow(new IllegalArgumentException("w"));
        mvc.perform(getStandardRequest(post(TEST_ENDPOINT), item1InsertDto, 1))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(15)
    void userUpdateError() throws Exception {
        when(service.update(any()))
                .thenThrow(new NoSuchElementException("no such element"));
        mvc.perform(getStandardRequest(patch(TEST_ENDPOINT + "/99"), item1InsertDto, 1))
                .andExpect(status().isNotFound());
    }
}
