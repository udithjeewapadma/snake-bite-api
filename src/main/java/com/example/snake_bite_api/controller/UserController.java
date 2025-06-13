package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateUserRequestDTO;
import com.example.snake_bite_api.controller.dto.response.UserResponseDTO;
import com.example.snake_bite_api.models.User;
import com.example.snake_bite_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUser(@Valid @RequestBody CreateUserRequestDTO createUserRequestDTO) {

        User user = userService.createUser(createUserRequestDTO);
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setUserName(user.getUserName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setPhoneNumber(user.getPhoneNumber());
        return userResponseDTO;
    }

    @GetMapping("/{user-id}")
    public UserResponseDTO getUserById(@PathVariable("user-id") Long userId) {
        return userService.findUserById(userId);
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.findAllUsers();
    }

    @DeleteMapping("/{user-id}")
    public void deleteUserById(@PathVariable("user-id") Long userId) {
        userService.deleteUserById(userId);
    }

    @PutMapping("/{user-id}")
    public UserResponseDTO updateUserById(@PathVariable("user-id") Long userId,
                                          @RequestBody CreateUserRequestDTO createUserRequestDTO) {

        User user = userService.updateUserById(userId, createUserRequestDTO);
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setUserName(user.getUserName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setPhoneNumber(user.getPhoneNumber());
        return userResponseDTO;
    }
}
