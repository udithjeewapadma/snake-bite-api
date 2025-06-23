package com.example.snake_bite_api.service;

import com.example.snake_bite_api.controller.dto.request.CreateReplyRequestDTO;
import com.example.snake_bite_api.models.Reply;

public interface ReplyService {

    Reply createReply(Long userId, Long commentId,CreateReplyRequestDTO createReplyRequestDTO);
}
