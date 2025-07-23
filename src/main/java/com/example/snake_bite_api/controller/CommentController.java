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
    public CommentResponseDTO createComment(@RequestParam Long userId,
                                            @RequestParam Long blogId,
                                            @RequestBody CreateCommentRequestDTO createCommentRequestDTO) {
        Comment comment = commentService.createComment(userId, blogId, createCommentRequestDTO);
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
        commentResponseDTO.setId(comment.getId());
        commentResponseDTO.setComment(comment.getCommentText());
        commentResponseDTO.setUserId(comment.getUser().getId());
        commentResponseDTO.setBlogId(comment.getBlog().getId());
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

    @DeleteMapping("/{comment-id}")
    public void deleteCommentById(@PathVariable("comment-id") Long commentId) {
        commentService.deleteCommentById(commentId);
    }

    @PutMapping("/{comment-id}")
    public CommentResponseDTO updateCommentById(@PathVariable("comment-id")  Long commentId,
                                                    @RequestBody CreateCommentRequestDTO createCommentRequestDTO) {
        Comment comment = commentService.updateCommentById(commentId, createCommentRequestDTO);
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
        commentResponseDTO.setId(comment.getId());
        commentResponseDTO.setComment(comment.getCommentText());
        commentResponseDTO.setUserId(comment.getUser().getId());
        commentResponseDTO.setBlogId(comment.getBlog().getId());
        return commentResponseDTO;
    }
}
