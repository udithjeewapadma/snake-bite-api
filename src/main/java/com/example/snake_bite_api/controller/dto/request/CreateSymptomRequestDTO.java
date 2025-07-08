package com.example.snake_bite_api.controller.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateSymptomRequestDTO {

    private String name;
    private String description;
    private List<Long> snakeIds;
}
