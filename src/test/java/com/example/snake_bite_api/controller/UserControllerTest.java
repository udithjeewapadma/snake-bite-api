package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateUserRequestDTO;
import com.example.snake_bite_api.controller.dto.response.UserResponseDTO;
import com.example.snake_bite_api.models.User;
import com.example.snake_bite_api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;  // For JSON serialization/deserialization

    private User user1;
    private User user2;

    private UserResponseDTO responseDTO1;
    private UserResponseDTO responseDTO2;

    private CreateUserRequestDTO createUserRequestDTO;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setUserName("User1");
        user1.setEmail("user1@example.com");
        user1.setPhoneNumber("0700000001");

        user2 = new User();
        user2.setId(2L);
        user2.setUserName("User2");
        user2.setEmail("user2@example.com");
        user2.setPhoneNumber("0700000002");

        responseDTO1 = new UserResponseDTO();
        responseDTO1.setId(user1.getId());
        responseDTO1.setUserName(user1.getUserName());
        responseDTO1.setEmail(user1.getEmail());
        responseDTO1.setPhoneNumber(user1.getPhoneNumber());

        responseDTO2 = new UserResponseDTO();
        responseDTO2.setId(user2.getId());
        responseDTO2.setUserName(user2.getUserName());
        responseDTO2.setEmail(user2.getEmail());
        responseDTO2.setPhoneNumber(user2.getPhoneNumber());

        createUserRequestDTO = new CreateUserRequestDTO();
        createUserRequestDTO.setUserName("NewUser");
        createUserRequestDTO.setEmail("newuser@example.com");
        createUserRequestDTO.setPhoneNumber("0700123456");
    }

    @Test
    void testCreateUser() throws Exception {
        User createdUser = new User();
        createdUser.setId(10L);
        createdUser.setUserName(createUserRequestDTO.getUserName());
        createdUser.setEmail(createUserRequestDTO.getEmail());
        createdUser.setPhoneNumber(createUserRequestDTO.getPhoneNumber());

        Mockito.when(userService.createUser(any(CreateUserRequestDTO.class))).thenReturn(createdUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.userName").value("NewUser"))
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("0700123456"));
    }

    @Test
    void testGetUserById() throws Exception {
        Mockito.when(userService.findUserById(1L)).thenReturn(responseDTO1);

        mockMvc.perform(get("/users/{user-id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDTO1.getId()))
                .andExpect(jsonPath("$.userName").value(responseDTO1.getUserName()))
                .andExpect(jsonPath("$.email").value(responseDTO1.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(responseDTO1.getPhoneNumber()));
    }

    @Test
    void testGetAllUsers() throws Exception {
        Mockito.when(userService.findAllUsers()).thenReturn(List.of(responseDTO1, responseDTO2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userName", is("User1")))
                .andExpect(jsonPath("$[1].userName", is("User2")));
    }

    @Test
    void testDeleteUserById() throws Exception {
        Mockito.doNothing().when(userService).deleteUserById(1L);

        mockMvc.perform(delete("/users/{user-id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateUserById() throws Exception {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUserName(createUserRequestDTO.getUserName());
        updatedUser.setEmail(createUserRequestDTO.getEmail());
        updatedUser.setPhoneNumber(createUserRequestDTO.getPhoneNumber());

        Mockito.when(userService.updateUserById(eq(1L), any(CreateUserRequestDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/{user-id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userName").value("NewUser"))
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("0700123456"));
    }
}
