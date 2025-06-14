package com.example.snake_bite_api.service;

import com.example.snake_bite_api.controller.dto.request.CreateSnakeRequestDTO;
import com.example.snake_bite_api.controller.dto.response.SnakeResponseDTO;
import com.example.snake_bite_api.exception.AdminNotFoundException;
import com.example.snake_bite_api.exception.SnakeNotFoundException;
import com.example.snake_bite_api.models.Snake;

import java.io.IOException;

public interface SnakeService {

    SnakeResponseDTO createSnake(Long adminId,
                                 CreateSnakeRequestDTO createSnakeRequestDTO) throws IOException, AdminNotFoundException;


    SnakeResponseDTO findSnakeById(Long id) throws SnakeNotFoundException;
}
