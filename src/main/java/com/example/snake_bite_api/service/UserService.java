package com.example.snake_bite_api.service;

import com.example.snake_bite_api.controller.dto.request.CreateUserRequestDTO;
import com.example.snake_bite_api.controller.dto.response.UserResponseDTO;
import com.example.snake_bite_api.exception.UserNotFoundException;
import com.example.snake_bite_api.models.User;

import java.util.List;

public interface UserService {

    User createUser(CreateUserRequestDTO createUserRequestDTO);

    UserResponseDTO findUserById(Long id) throws UserNotFoundException;

    List<UserResponseDTO> findAllUsers();

    void deleteUserById(Long id);

    User updateUserById(Long id, CreateUserRequestDTO createUserRequestDTO) throws UserNotFoundException;
}
