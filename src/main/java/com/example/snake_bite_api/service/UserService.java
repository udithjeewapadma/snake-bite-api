package com.example.snake_bite_api.service;

import com.example.snake_bite_api.controller.dto.request.CreateUserRequestDTO;
import com.example.snake_bite_api.controller.dto.response.UserResponseDTO;
import com.example.snake_bite_api.models.User;

import java.util.List;

public interface UserService {

    User createUser(CreateUserRequestDTO createUserRequestDTO);

    UserResponseDTO findUserById(Long id);

    List<UserResponseDTO> findAllUsers();
}
