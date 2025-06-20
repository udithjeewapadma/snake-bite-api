package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateCommentRequestDTO;
import com.example.snake_bite_api.controller.dto.response.CommentResponseDTO;
import com.example.snake_bite_api.models.Comment;
import com.example.snake_bite_api.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDTO createComment(@RequestParam Long userId, @RequestBody CreateCommentRequestDTO createCommentRequestDTO) {
        Comment comment = commentService.createComment(userId, createCommentRequestDTO);
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
        commentResponseDTO.setId(comment.getId());
        commentResponseDTO.setComment(comment.getComment());
        commentResponseDTO.setUserId(comment.getUser().getId());
        return commentResponseDTO;
    }

    @GetMapping("/{comment-id}")
    public CommentResponseDTO findCommentById(@PathVariable("comment-id") Long commentId) {
        return commentService.findCommentById(commentId);
    }

    @GetMapping
    public List<CommentResponseDTO> findAllComments() {
        return commentService.findAllComments();
    }
}
