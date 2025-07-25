package com.example.snake_bite_api.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class FirstAidResponseDTO {

    private Long id;
    private String name;
    private String description;
    private Long adminId;
    private Long symptomId;
}
