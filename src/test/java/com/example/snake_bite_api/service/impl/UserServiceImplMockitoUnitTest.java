package com.example.snake_bite_api.service.impl;

import com.example.snake_bite_api.controller.dto.request.CreateUserRequestDTO;
import com.example.snake_bite_api.controller.dto.response.UserResponseDTO;
import com.example.snake_bite_api.exception.UserNotFoundException;
import com.example.snake_bite_api.models.User;
import com.example.snake_bite_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplMockitoUnitTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        CreateUserRequestDTO dto = new CreateUserRequestDTO();
        dto.setUserName("John");
        dto.setEmail("john@example.com");
        dto.setPhoneNumber("0771234567");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUserName(dto.getUserName());
        savedUser.setEmail(dto.getEmail());
        savedUser.setPhoneNumber(dto.getPhoneNumber());

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createUser(dto);

        assertNotNull(result);
        assertEquals("John", result.getUserName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFindUserById_UserExists() {
        User user = new User();
        user.setId(1L);
        user.setUserName("Alice");
        user.setEmail("alice@example.com");
        user.setPhoneNumber("0711111111");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO response = userService.findUserById(1L);

        assertEquals("Alice", response.getUserName());
        assertEquals("alice@example.com", response.getEmail());
    }

    @Test
    void testFindUserById_UserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(99L));
    }

    @Test
    void testFindAllUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUserName("A");
        user1.setEmail("a@email.com");
        user1.setPhoneNumber("0711");

        User user2 = new User();
        user2.setId(2L);
        user2.setUserName("B");
        user2.setEmail("b@email.com");
        user2.setPhoneNumber("0722");


        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserResponseDTO> users = userService.findAllUsers();

        assertEquals(2, users.size());
    }

    @Test
    void testDeleteUserById_UserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUserById(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void testDeleteUserById_UserNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(99L));
    }

    @Test
    void testUpdateUserById_UserExists() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUserName("old");
        user1.setEmail("old@email.com");
        user1.setPhoneNumber("0700");

        CreateUserRequestDTO dto = new CreateUserRequestDTO();
        dto.setUserName("New");
        dto.setEmail("new@email.com");
        dto.setPhoneNumber("0788888888");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User updated = userService.updateUserById(1L, dto);

        assertEquals("New", updated.getUserName());
        assertEquals("new@email.com", updated.getEmail());
    }
}
