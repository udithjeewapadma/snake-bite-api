package com.example.snake_bite_api.controller.dto.response;

import com.example.snake_bite_api.models.SnakeRequestStatus;
import com.example.snake_bite_api.models.Venomous;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewSnakeResponseDTO {
    private Long id;
    private String name;
    private String species;
    private String color;
    private String pattern;
    private double averageLength;
    private Venomous venomous;
    private List<String> imageUrls;
    private SnakeRequestStatus status;

    private Long userId;
    private Long adminId;
}
