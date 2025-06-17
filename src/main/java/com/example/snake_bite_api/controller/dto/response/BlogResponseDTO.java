package com.example.snake_bite_api.controller.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BlogResponseDTO {
    private Long id;
    private String title;
    private String content;
    private List<String> imageUrls;

    private Long userId;
}
