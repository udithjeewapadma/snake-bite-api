package com.example.snake_bite_api.controller.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentResponseDTO {

    private Long id;
    private String commentText;
    private long userId;
    private long blogId;
}
