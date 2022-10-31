package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestGetDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.UserControllerTest;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.Constants;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.utils.Constants.USER_HTTP_HEADER;
import static ru.practicum.shareit.utils.EndpointPaths.ITEM_REQUEST_ENDPOINT;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {
    public static final String TEST_ENDPOINT = ITEM_REQUEST_ENDPOINT;

    @MockBean
    ItemRequestService service;

    @MockBean
    UserService userService;

    @MockBean
    ItemRequestMapper itemRequestMapper;
    @Autowired
    ObjectMapper mapper;
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
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void beforeEach() {
        for (int i = 0; i < 3; i++) {
            when(itemRequestMapper.toGetDto(itemRequests.get(i)))
                    .thenReturn(returnedItemRequest.get(i));
            when(itemRequestMapper.toItemRequest(itemRequestCreateDtos.get(i)))
                    .thenReturn(itemRequests.get(i));
        }
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
    void save() throws Exception {
        when(service.create(any()))
                .thenReturn(itemRequests.get(0));
        mvc.perform(getStandardRequest(post(TEST_ENDPOINT), itemRequestCreateDtos.get(0), 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnedItemRequest.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.description", is(returnedItemRequest.get(0).getDescription())))
                .andExpect(jsonPath("$.items.length()", is(0)));
    }

    @Test
    @Order(5)
    void getAllRequester() throws Exception {
        when(userService.findById(any()))
                .thenReturn(Optional.ofNullable(UserControllerTest.returnedUsers.get(0)));
        when(service.findAllByRequesterId(1L, PageRequest.of(0, Integer.parseInt(Constants.DEFAULT_PAGE_SIZE))))
                .thenReturn(List.of(itemRequests.get(0)));
        mvc.perform(getStandardRequest(get(TEST_ENDPOINT), null, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(returnedItemRequest.get(0).getId()), Long.class));
    }

    @Test
    @Order(6)
    void getAllExceptRequester() throws Exception {
        when(userService.findById(any()))
                .thenReturn(Optional.ofNullable(UserControllerTest.returnedUsers.get(0)));
        when(service.findAllExceptRequester(1L,
                PageRequest.of(0,
                        Integer.parseInt(Constants.DEFAULT_PAGE_SIZE),
                        Sort.Direction.DESC,
                        "created")))
                .thenReturn(List.of(itemRequests.get(1), itemRequests.get(2)));
        mvc.perform(getStandardRequest(get(TEST_ENDPOINT + "/all"), null, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(returnedItemRequest.get(1).getId()), Long.class))
                .andExpect(jsonPath("$[1].id", is(returnedItemRequest.get(2).getId()), Long.class));
    }

    @Test
    void getById() throws Exception {
        when(service.findById(any(), any()))
                .thenReturn(Optional.of(itemRequests.get(0)));
        mvc.perform(getStandardRequest(get(TEST_ENDPOINT + "/1"), null, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnedItemRequest.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.description", is(returnedItemRequest.get(0).getDescription())))
                .andExpect(jsonPath("$.created", is(returnedItemRequest.get(0).getCreated().toString())))
                .andExpect(jsonPath("$.items.length()", is(0)));
    }
}
