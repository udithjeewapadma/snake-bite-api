package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateSnakeRequestDTO;
import com.example.snake_bite_api.controller.dto.response.SnakeResponseDTO;
import com.example.snake_bite_api.service.SnakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/snakes")
public class SnakeController {

    @Autowired
    SnakeService snakeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private SnakeResponseDTO createSnake(@RequestParam Long adminId,
                                         @ModelAttribute CreateSnakeRequestDTO createSnakeRequestDTO)
            throws IOException {
        return snakeService.createSnake(adminId, createSnakeRequestDTO);

    }

    @GetMapping("/{snake-id}")
    private SnakeResponseDTO getSnakeById(@PathVariable("snake-id") Long snakeId){
        return snakeService.findSnakeById(snakeId);
    }
}
