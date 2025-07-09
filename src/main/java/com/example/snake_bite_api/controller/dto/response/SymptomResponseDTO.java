package com.example.snake_bite_api.controller.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SymptomResponseDTO {

    private Long id;
    private String name;
    private String description;
    private List<SnakeDTO> snakeList;
    private Long adminId;
}
