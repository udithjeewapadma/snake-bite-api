package com.example.snake_bite_api.service;

import com.example.snake_bite_api.controller.dto.request.CreateCommentRequestDTO;
import com.example.snake_bite_api.controller.dto.response.CommentResponseDTO;
import com.example.snake_bite_api.models.Comment;

import java.util.List;

public interface CommentService {

    Comment createComment(Long userId,CreateCommentRequestDTO createCommentRequestDTO);

    CommentResponseDTO findCommentById(Long id);

    List<CommentResponseDTO> findAllComments();
}
