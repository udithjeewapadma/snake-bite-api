package com.example.snake_bite_api.service;

import com.example.snake_bite_api.controller.dto.request.CreateUserRequestDTO;
import com.example.snake_bite_api.models.User;

public interface UserService {

    User createUser(CreateUserRequestDTO createUserRequestDTO);
}
