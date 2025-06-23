package com.example.snake_bite_api.service.impl;

import com.example.snake_bite_api.controller.dto.request.CreateCommentRequestDTO;
import com.example.snake_bite_api.controller.dto.response.CommentResponseDTO;
import com.example.snake_bite_api.exception.BlogNotFoundException;
import com.example.snake_bite_api.exception.CommentNotFoundException;
import com.example.snake_bite_api.exception.UserNotFoundException;
import com.example.snake_bite_api.models.Blog;
import com.example.snake_bite_api.models.Comment;
import com.example.snake_bite_api.models.User;
import com.example.snake_bite_api.repository.BlogRepository;
import com.example.snake_bite_api.repository.CommentRepository;
import com.example.snake_bite_api.repository.UserRepository;
import com.example.snake_bite_api.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment createComment(Long userId, Long blogId, CreateCommentRequestDTO createCommentRequestDTO)
                    throws UserNotFoundException{

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new BlogNotFoundException("Blog with id " + blogId + " not found"));

        Comment comment = new Comment();
        comment.setComment(createCommentRequestDTO.getComment());
        comment.setUser(user);
        comment.setBlog(blog);
        return commentRepository.save(comment);

    }

    @Override
    public CommentResponseDTO findCommentById(Long id) throws CommentNotFoundException {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + id + " not found"));
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
        commentResponseDTO.setId(comment.getId());
        commentResponseDTO.setComment(comment.getComment());
        commentResponseDTO.setUserId(comment.getUser().getId());
        commentResponseDTO.setBlogId(comment.getBlog().getId());
        return commentResponseDTO;

    }

    @Override
    public List<CommentResponseDTO> findAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream().map(comment -> {
            CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
            commentResponseDTO.setId(comment.getId());
            commentResponseDTO.setComment(comment.getComment());
            commentResponseDTO.setUserId(comment.getUser().getId());
            commentResponseDTO.setBlogId(comment.getBlog().getId());
            return commentResponseDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteCommentById(Long id) throws CommentNotFoundException {
        if (!commentRepository.existsById(id)) {
            throw new CommentNotFoundException("Comment with id " + id + " not found");
        }
        commentRepository.deleteById(id);
    }

    @Override
    public Comment updateCommentById(Long id, CreateCommentRequestDTO createCommentRequestDTO) {

        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + id + " not found"));
        existingComment.setComment(createCommentRequestDTO.getComment());
        return commentRepository.save(existingComment);
    }
}
