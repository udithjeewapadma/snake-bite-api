package com.example.snake_bite_api.service.impl;

import com.example.snake_bite_api.controller.dto.request.CreateCommentRequestDTO;
import com.example.snake_bite_api.controller.dto.response.CommentResponseDTO;
import com.example.snake_bite_api.exception.CommentNotFoundException;
import com.example.snake_bite_api.exception.UserNotFoundException;
import com.example.snake_bite_api.models.Comment;
import com.example.snake_bite_api.models.User;
import com.example.snake_bite_api.repository.CommentRepository;
import com.example.snake_bite_api.repository.UserRepository;
import com.example.snake_bite_api.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment createComment(Long userId, CreateCommentRequestDTO createCommentRequestDTO) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        Comment comment = new Comment();
        comment.setComment(createCommentRequestDTO.getComment());
        comment.setUser(user);
        return commentRepository.save(comment);

    }

    @Override
    public CommentResponseDTO findCommentById(Long id) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + id + " not found"));
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
        commentResponseDTO.setId(comment.getId());
        commentResponseDTO.setComment(comment.getComment());
        commentResponseDTO.setUserId(comment.getUser().getId());
        return commentResponseDTO;

    }
}
