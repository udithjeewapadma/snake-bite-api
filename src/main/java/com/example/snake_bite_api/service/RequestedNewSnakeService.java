package com.example.snake_bite_api.service;

import com.example.snake_bite_api.controller.dto.request.CreateNewSnakeRequestDTO;
import com.example.snake_bite_api.controller.dto.response.AdminInteractionRequestedSnakeResponseDTO;
import com.example.snake_bite_api.controller.dto.response.NewSnakeResponseDTO;
import com.example.snake_bite_api.exception.UserNotFoundException;

import java.io.IOException;
import java.util.List;

public interface RequestedNewSnakeService {

    NewSnakeResponseDTO createRequest(Long userId, CreateNewSnakeRequestDTO createNewSnakeRequestDTO)
            throws UserNotFoundException, IOException;

    List<NewSnakeResponseDTO> findAllRequests();
}
