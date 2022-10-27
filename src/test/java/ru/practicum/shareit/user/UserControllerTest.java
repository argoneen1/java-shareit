package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.practicum.shareit.user.dto.UserInsertDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.utils.EndpointPaths.USER_ENDPOINT;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    public static final String TEST_ENDPOINT = USER_ENDPOINT;
    public static List<UserInsertDto> userInsertDtos = List.of(
            new UserInsertDto(null, "user1name", "user1@email.test"),
            new UserInsertDto(null, "user2name", "user2@email.test"),
            new UserInsertDto(null, "user3name", "user3@email.test")
    );
    public static List<User> returnedUsers = List.of(
            new User(1L, "user1name", "user1@email.test"),
            new User(2L, "user2name", "user2@email.test"),
            new User(3L, "user3name", "user3@email.test")
    );
    @MockBean
    UserService service;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    public <T> ResultActions getStandardRequest(MockHttpServletRequestBuilder requestBuilders,
                                                T mappedValue)
            throws Exception {
        return mvc.perform(
                requestBuilders
                        .content(mapper.writeValueAsString(mappedValue))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(1)
    void saveNewUser() throws Exception {
        when(service.create(any()))
                .thenReturn(returnedUsers.get(0));
        getStandardRequest(post(TEST_ENDPOINT), userInsertDtos.get(0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnedUsers.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.name", is(returnedUsers.get(0).getName())))
                .andExpect(jsonPath("$.email", is(returnedUsers.get(0).getEmail())));
    }

    @Test
    @Order(2)
    void saveUser2() throws Exception {
        when(service.create(any()))
                .thenReturn(returnedUsers.get(1));
        getStandardRequest(post(TEST_ENDPOINT), userInsertDtos.get(1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnedUsers.get(1).getId()), Long.class))
                .andExpect(jsonPath("$.name", is(returnedUsers.get(1).getName())))
                .andExpect(jsonPath("$.email", is(returnedUsers.get(1).getEmail())));
    }

    @Test
    @Order(3)
    void saveUser3() throws Exception {
        when(service.create(any()))
                .thenReturn(returnedUsers.get(2));
        getStandardRequest(post(TEST_ENDPOINT), userInsertDtos.get(2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnedUsers.get(2).getId()), Long.class))
                .andExpect(jsonPath("$.name", is(returnedUsers.get(2).getName())))
                .andExpect(jsonPath("$.email", is(returnedUsers.get(2).getEmail())));
    }

    @Test
    @Order(4)
    void saveUserErrorDuplicateEmail() throws Exception {
        when(service.create(any()))
                .thenThrow(new IllegalArgumentException("a"));
        getStandardRequest(post(TEST_ENDPOINT), userInsertDtos.get(2))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    void getAll() throws Exception {
        when(service.findAll(any()))
                .thenReturn(returnedUsers);
        getStandardRequest(get(TEST_ENDPOINT), null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(returnedUsers.get(0).getId()), Long.class))
                .andExpect(jsonPath("$[1].id", is(returnedUsers.get(1).getId()), Long.class))
                .andExpect(jsonPath("$[2].id", is(returnedUsers.get(2).getId()), Long.class));
    }

    @Test
    @Order(6)
    void getByIdError() throws Exception {
        getStandardRequest(get(TEST_ENDPOINT + "/99"), null)
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(7)
    void getUser1() throws Exception {
        when(service.findById(1L))
                .thenReturn(Optional.of(returnedUsers.get(0)));
        System.out.println(returnedUsers.get(0).getId());
        getStandardRequest(get(TEST_ENDPOINT + "/1"), null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnedUsers.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.name", is(returnedUsers.get(0).getName())))
                .andExpect(jsonPath("$.email", is(returnedUsers.get(0).getEmail())));
    }

    @Test
    @Order(8)
    void getUser2() throws Exception {
        when(service.findById(any()))
                .thenReturn(Optional.of(returnedUsers.get(1)));
        getStandardRequest(get(TEST_ENDPOINT + "/1"), null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnedUsers.get(1).getId()), Long.class))
                .andExpect(jsonPath("$.name", is(returnedUsers.get(1).getName())))
                .andExpect(jsonPath("$.email", is(returnedUsers.get(1).getEmail())));
    }

    @Test
    @Order(9)
    void updateUserName() throws Exception {
        User returnedUser = new User(1L, "user1nameUpdated", "user1@email.test");
        UserInsertDto insertedDto = new UserInsertDto(null, "user1nameUpdated", null);
        when(service.update(any()))
                .thenReturn(returnedUser);
        getStandardRequest(patch(TEST_ENDPOINT + "/1"), insertedDto).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnedUser.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(returnedUser.getName())))
                .andExpect(jsonPath("$.email", is(returnedUser.getEmail())));
    }

    @Test
    @Order(10)
    void updateUserEmail() throws Exception {
        User returnedUser = new User(1L, "user1nameUpdated", "user1Updated@email.test");
        User insertedDto = new User(1L, null, "user1Updated@email.test");
        when(service.update(any()))
                .thenReturn(returnedUser);
        getStandardRequest(patch(TEST_ENDPOINT + "/1"), insertedDto).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnedUser.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(returnedUser.getName())))
                .andExpect(jsonPath("$.email", is(returnedUser.getEmail())));
    }

    @Test
    @Order(11)
    void updateUserAll() throws Exception {
        User returnedUser = new User(1L, "user1nameUpdated1", "user1Updated1@email.test");
        User insertedDto = new User(1L, "user1nameUpdated1", "user1Updated1@email.test");
        when(service.update(any()))
                .thenReturn(returnedUser);
        getStandardRequest(patch(TEST_ENDPOINT + "/1"), insertedDto).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnedUser.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(returnedUser.getName())))
                .andExpect(jsonPath("$.email", is(returnedUser.getEmail())));
    }

    @Test
    @Order(12)
    void userDelete() throws Exception {

        getStandardRequest(delete(TEST_ENDPOINT + "/1"), null)
                .andExpect(status().isOk());
        getStandardRequest(get(TEST_ENDPOINT + "/1"), null)
                .andExpect(status().isNotFound());
        verify(service, times(1)).delete(any());
    }

    @Test
    @Order(13)
    void userDeleteError() throws Exception {
        getStandardRequest(delete(TEST_ENDPOINT + "/99"), null)
                .andExpect(status().isOk());
        verify(service, times(1)).delete(any());
    }

    @Test
    @Order(14)
    void userCreateError() throws Exception {
        when(service.create(any()))
                .thenThrow(new IllegalArgumentException("w"));
        getStandardRequest(post(TEST_ENDPOINT), userInsertDtos.get(0))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(15)
    void userUpdateError() throws Exception {
        when(service.update(any()))
                .thenThrow(new NoSuchElementException("no such element"));
        getStandardRequest(patch(TEST_ENDPOINT + "/99"), userInsertDtos.get(0))
                .andExpect(status().isNotFound());
    }
}
