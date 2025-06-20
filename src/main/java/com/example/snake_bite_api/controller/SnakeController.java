package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateSnakeRequestDTO;
import com.example.snake_bite_api.controller.dto.response.SnakeResponseDTO;
import com.example.snake_bite_api.service.SnakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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

    @GetMapping
    private List<SnakeResponseDTO> findAllSnake(){
        return snakeService.findAllSnake();
    }

    @DeleteMapping("/{snake-id}")
    private void deleteSnakeById(@PathVariable("snake-id") Long snakeId){
        snakeService.deleteSnakeById(snakeId);
    }

    @PutMapping("/{snake-id}")
    public SnakeResponseDTO updateSnakeById(@PathVariable("snake-id") Long id,
                                    @ModelAttribute CreateSnakeRequestDTO createSnakeRequestDTO) throws IOException {
        return snakeService.updateSnakeById(id, createSnakeRequestDTO);
    }
}
