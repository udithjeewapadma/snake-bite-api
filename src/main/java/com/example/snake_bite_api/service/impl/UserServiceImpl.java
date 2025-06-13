package com.example.snake_bite_api.service.impl;

import com.example.snake_bite_api.controller.dto.request.CreateUserRequestDTO;
import com.example.snake_bite_api.controller.dto.response.UserResponseDTO;
import com.example.snake_bite_api.exception.UserNotFoundException;
import com.example.snake_bite_api.models.User;
import com.example.snake_bite_api.repository.UserRepository;
import com.example.snake_bite_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(CreateUserRequestDTO createUserRequestDTO) {

        User user = new User();
        user.setUserName(createUserRequestDTO.getUserName());
        user.setEmail(createUserRequestDTO.getEmail());
        user.setPhoneNumber(createUserRequestDTO.getPhoneNumber());

        return userRepository.save(user);
    }

    @Override
    public UserResponseDTO findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setUserName(user.getUserName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setPhoneNumber(user.getPhoneNumber());
        return userResponseDTO;

    }
}
