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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceImplMockitoUnitTest {

    private UserRepository userRepository;
    private BlogRepository blogRepository;
    private CommentRepository commentRepository;

    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        blogRepository = mock(BlogRepository.class);
        commentRepository = mock(CommentRepository.class);
        commentService = new CommentServiceImpl(userRepository, blogRepository, commentRepository);
    }

    @Test
    void createComment_Success() {
        Long userId = 1L;
        Long blogId = 2L;

        User user = new User();
        user.setId(userId);

        Blog blog = new Blog();
        blog.setId(blogId);

        CreateCommentRequestDTO dto = new CreateCommentRequestDTO();
        dto.setCommentText("Nice blog!");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));
        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> inv.getArgument(0));

        Comment saved = commentService.createComment(userId, blogId, dto);

        assertEquals("Nice blog!", saved.getCommentText());
        assertEquals(user, saved.getUser());
        assertEquals(blog, saved.getBlog());
    }

    @Test
    void createComment_UserNotFound_Throws() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                commentService.createComment(1L, 1L, new CreateCommentRequestDTO()));
    }

    @Test
    void findCommentById_Success() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setCommentText("Test Comment");

        User user = new User();
        user.setId(5L);

        Blog blog = new Blog();
        blog.setId(9L);

        comment.setUser(user);
        comment.setBlog(blog);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        CommentResponseDTO dto = commentService.findCommentById(1L);

        assertEquals(1L, dto.getId());
        assertEquals("Test Comment", dto.getCommentText());
        assertEquals(5L, dto.getUserId());
        assertEquals(9L, dto.getBlogId());
    }

    @Test
    void findCommentById_NotFound_Throws() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CommentNotFoundException.class, () -> commentService.findCommentById(1L));
    }

    @Test
    void findAllComments_ReturnsAll() {
        Comment c1 = new Comment();
        c1.setId(1L);
        c1.setCommentText("A");

        User user = new User();
        user.setId(1L);

        Blog blog = new Blog();
        blog.setId(2L);

        c1.setUser(user);
        c1.setBlog(blog);

        when(commentRepository.findAll()).thenReturn(List.of(c1));

        List<CommentResponseDTO> result = commentService.findAllComments();
        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getCommentText());
    }

    @Test
    void deleteCommentById_Success() {
        when(commentRepository.existsById(10L)).thenReturn(true);
        commentService.deleteCommentById(10L);
        verify(commentRepository).deleteById(10L);
    }

    @Test
    void deleteCommentById_NotFound_Throws() {
        when(commentRepository.existsById(99L)).thenReturn(false);
        assertThrows(CommentNotFoundException.class, () -> commentService.deleteCommentById(99L));
    }

    @Test
    void updateCommentById_Success() {
        Comment existing = new Comment();
        existing.setId(4L);
        existing.setCommentText("Old");

        CreateCommentRequestDTO dto = new CreateCommentRequestDTO();
        dto.setCommentText("New");

        when(commentRepository.findById(4L)).thenReturn(Optional.of(existing));
        when(commentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Comment updated = commentService.updateCommentById(4L, dto);
        assertEquals("New", updated.getCommentText());
    }
}
