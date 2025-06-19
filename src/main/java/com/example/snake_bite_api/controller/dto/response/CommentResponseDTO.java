package com.example.snake_bite_api.controller.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentResponseDTO {

    private Long id;
    private String comment;
    private long userId;
}
