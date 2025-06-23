package com.example.snake_bite_api.controller.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReplyResponseDTO {

    private Long Id;
    private String replyBody;
    private Long userId;
    private Long commentId;
}
