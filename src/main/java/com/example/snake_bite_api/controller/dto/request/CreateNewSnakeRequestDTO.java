package com.example.snake_bite_api.controller.dto.request;

import com.example.snake_bite_api.models.SnakeRequestStatus;
import com.example.snake_bite_api.models.Venomous;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CreateNewSnakeRequestDTO {

    private String name;
    private String species;
    private String color;
    private String pattern;
    private double averageLength;
    private Venomous venomous;

    private List<MultipartFile> imageFiles;
    private SnakeRequestStatus status;
}
