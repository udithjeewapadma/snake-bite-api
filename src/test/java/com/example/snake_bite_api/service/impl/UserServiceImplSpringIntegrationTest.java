package com.example.snake_bite_api.service.impl;

import com.example.snake_bite_api.controller.dto.request.CreateUserRequestDTO;
import com.example.snake_bite_api.controller.dto.response.UserResponseDTO;
import com.example.snake_bite_api.exception.UserNotFoundException;
import com.example.snake_bite_api.models.User;
import com.example.snake_bite_api.repository.UserRepository;
import com.example.snake_bite_api.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceImplSpringIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testCreateAndFindUser() {
        CreateUserRequestDTO dto = new CreateUserRequestDTO();
        dto.setUserName("Bob");
        dto.setEmail("bob@test.com");
        dto.setPhoneNumber("071‑2222222");

        User saved = userService.createUser(dto);

        UserResponseDTO fetched = userService.findUserById(saved.getId());

        assertEquals("Bob", fetched.getUserName());
    }

    @Test
    void testFindAllUsers() {
        CreateUserRequestDTO dto1 = new CreateUserRequestDTO();
        dto1.setUserName("X");
        dto1.setEmail("x@test.com");
        dto1.setPhoneNumber("0711");
        userService.createUser(dto1);

        CreateUserRequestDTO dto2 = new CreateUserRequestDTO();
        dto2.setUserName("Y");
        dto2.setEmail("y@test.com");
        dto2.setPhoneNumber("0722");
        userService.createUser(dto2);

        List<UserResponseDTO> users = userService.findAllUsers();
        assertTrue(users.size() >= 2);
    }

    @Test
    void testUpdateUser() {
        CreateUserRequestDTO dto = new CreateUserRequestDTO();
        dto.setUserName("Original");
        dto.setEmail("original@test.com");
        dto.setPhoneNumber("0700");

        User user = userService.createUser(dto);

        CreateUserRequestDTO updateDto = new CreateUserRequestDTO();
        updateDto.setUserName("Updated");
        updateDto.setEmail("updated@test.com");
        updateDto.setPhoneNumber("0701");

        User updated = userService.updateUserById(user.getId(), updateDto);

        assertEquals("Updated", updated.getUserName());
    }

    @Test
    void testDeleteUser() {
        CreateUserRequestDTO dto = new CreateUserRequestDTO();
        dto.setUserName("ToDelete");
        dto.setEmail("delete@test.com");
        dto.setPhoneNumber("0777");

        User user = userService.createUser(dto);
        userService.deleteUserById(user.getId());

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(user.getId()));
    }
}
