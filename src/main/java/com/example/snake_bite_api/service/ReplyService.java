package com.example.snake_bite_api.service;

import com.example.snake_bite_api.controller.dto.request.CreateReplyRequestDTO;
import com.example.snake_bite_api.controller.dto.response.ReplyResponseDTO;
import com.example.snake_bite_api.exception.CommentNotFoundException;
import com.example.snake_bite_api.exception.ReplyNotFoundException;
import com.example.snake_bite_api.exception.UserNotFoundException;
import com.example.snake_bite_api.models.Reply;

import java.util.List;

public interface ReplyService {

    Reply createReply(Long userId, Long commentId,CreateReplyRequestDTO createReplyRequestDTO)
            throws UserNotFoundException, CommentNotFoundException;

    ReplyResponseDTO findReplyById(Long id) throws ReplyNotFoundException;

    List<ReplyResponseDTO> findAllReplies();

    void deleteReplyById(Long id) throws ReplyNotFoundException;

    Reply updateReplyById(Long id, CreateReplyRequestDTO createReplyRequestDTO) throws ReplyNotFoundException;
}
