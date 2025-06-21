package com.example.snake_bite_api.service;

import com.example.snake_bite_api.controller.dto.request.CreateCommentRequestDTO;
import com.example.snake_bite_api.controller.dto.response.CommentResponseDTO;
import com.example.snake_bite_api.exception.CommentNotFoundException;
import com.example.snake_bite_api.exception.UserNotFoundException;
import com.example.snake_bite_api.models.Comment;

import java.util.List;

public interface CommentService {

    Comment createComment(Long userId,CreateCommentRequestDTO createCommentRequestDTO) throws UserNotFoundException;

    CommentResponseDTO findCommentById(Long id) throws CommentNotFoundException;

    List<CommentResponseDTO> findAllComments();

    void deleteCommentById(Long id) throws CommentNotFoundException;

    Comment updateCommentById(Long id, CreateCommentRequestDTO createCommentRequestDTO);
}
