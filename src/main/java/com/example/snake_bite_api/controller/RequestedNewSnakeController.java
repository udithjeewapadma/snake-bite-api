package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateNewSnakeRequestDTO;
import com.example.snake_bite_api.controller.dto.response.AdminInteractionRequestedSnakeResponseDTO;
import com.example.snake_bite_api.controller.dto.response.NewSnakeResponseDTO;
import com.example.snake_bite_api.exception.UserNotFoundException;
import com.example.snake_bite_api.service.RequestedNewSnakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/newSnakes")
public class RequestedNewSnakeController {

    @Autowired
    private RequestedNewSnakeService requestedNewSnakeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private NewSnakeResponseDTO createRequest(@RequestParam Long userId,
                                              @ModelAttribute CreateNewSnakeRequestDTO createNewSnakeRequestDTO)
            throws IOException, UserNotFoundException {
        return requestedNewSnakeService.createRequest(userId, createNewSnakeRequestDTO);

    }

    @GetMapping
    private List<NewSnakeResponseDTO> findAllRequests() {
        return requestedNewSnakeService.findAllRequests();
    }
}
