package com.example.snake_bite_api.service.impl;

import com.example.snake_bite_api.controller.dto.request.CreateCommentRequestDTO;
import com.example.snake_bite_api.controller.dto.response.CommentResponseDTO;
import com.example.snake_bite_api.exception.CommentNotFoundException;
import com.example.snake_bite_api.exception.UserNotFoundException;
import com.example.snake_bite_api.models.Blog;
import com.example.snake_bite_api.models.Comment;
import com.example.snake_bite_api.models.User;
import com.example.snake_bite_api.repository.BlogRepository;
import com.example.snake_bite_api.repository.CommentRepository;
import com.example.snake_bite_api.repository.UserRepository;
import com.example.snake_bite_api.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceImplSpringIntegrationTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CommentRepository commentRepository;

    private Long userId;
    private Long blogId;

    @BeforeEach
    void setup() {
        User user = new User();
        user.setUserName("integration_user");
        userId = userRepository.save(user).getId();

        Blog blog = new Blog();
        blog.setTitle("Blog title");
        blog.setContent("Blog content");
        blog.setUser(user);
        blogId = blogRepository.save(blog).getId();
    }

    @Test
    void createComment_Success() throws UserNotFoundException {
        CreateCommentRequestDTO dto = new CreateCommentRequestDTO();
        dto.setComment("This is an integration comment");

        Comment comment = commentService.createComment(userId, blogId, dto);

        assertThat(comment).isNotNull();
        assertThat(comment.getComment()).isEqualTo(dto.getComment());
        assertThat(comment.getUser().getId()).isEqualTo(userId);
        assertThat(comment.getBlog().getId()).isEqualTo(blogId);
    }

    @Test
    void findCommentById_Success() throws Exception {
        Comment comment = new Comment();
        comment.setComment("Integration comment");
        comment.setUser(userRepository.findById(userId).orElseThrow());
        comment.setBlog(blogRepository.findById(blogId).orElseThrow());
        Comment saved = commentRepository.save(comment);

        CommentResponseDTO found = commentService.findCommentById(saved.getId());

        assertEquals(saved.getId(), found.getId());
        assertEquals("Integration comment", found.getComment());
    }

    @Test
    void deleteCommentById_Success() {
        Comment comment = new Comment();
        comment.setComment("To delete");
        comment.setUser(userRepository.findById(userId).orElseThrow());
        comment.setBlog(blogRepository.findById(blogId).orElseThrow());
        Comment saved = commentRepository.save(comment);

        commentService.deleteCommentById(saved.getId());

        assertFalse(commentRepository.existsById(saved.getId()));
    }

    @Test
    void findAllComments_ReturnsList() {
        Comment comment = new Comment();
        comment.setComment("A comment");
        comment.setUser(userRepository.findById(userId).orElseThrow());
        comment.setBlog(blogRepository.findById(blogId).orElseThrow());
        commentRepository.save(comment);

        List<CommentResponseDTO> all = commentService.findAllComments();
        assertThat(all).isNotEmpty();
    }

    @Test
    void updateCommentById_Success() {
        Comment comment = new Comment();
        comment.setComment("Old");
        comment.setUser(userRepository.findById(userId).orElseThrow());
        comment.setBlog(blogRepository.findById(blogId).orElseThrow());
        Comment saved = commentRepository.save(comment);

        CreateCommentRequestDTO update = new CreateCommentRequestDTO();
        update.setComment("Updated Comment");

        Comment updated = commentService.updateCommentById(saved.getId(), update);
        assertEquals("Updated Comment", updated.getComment());
    }

    @Test
    void findCommentById_NotFound_Throws() {
        assertThrows(CommentNotFoundException.class, () -> commentService.findCommentById(999L));
    }
}
