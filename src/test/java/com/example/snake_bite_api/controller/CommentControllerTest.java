package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateCommentRequestDTO;
import com.example.snake_bite_api.controller.dto.response.CommentResponseDTO;
import com.example.snake_bite_api.models.Comment;
import com.example.snake_bite_api.models.User;
import com.example.snake_bite_api.models.Blog;
import com.example.snake_bite_api.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Comment comment;
    private CommentResponseDTO commentResponseDTO;
    private CreateCommentRequestDTO createCommentRequestDTO;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1L);

        Blog blog = new Blog();
        blog.setId(1L);

        comment = new Comment();
        comment.setId(1L);
        comment.setComment("This is a comment");
        comment.setUser(user);
        comment.setBlog(blog);

        commentResponseDTO = new CommentResponseDTO();
        commentResponseDTO.setId(comment.getId());
        commentResponseDTO.setComment(comment.getComment());
        commentResponseDTO.setUserId(user.getId());
        commentResponseDTO.setBlogId(blog.getId());

        createCommentRequestDTO = new CreateCommentRequestDTO();
        createCommentRequestDTO.setComment("This is a comment");
    }

    @Test
    void testCreateComment() throws Exception {
        Mockito.when(commentService.createComment(anyLong(), anyLong(), any(CreateCommentRequestDTO.class)))
                .thenReturn(comment);

        mockMvc.perform(post("/comments")
                        .param("userId", "1")
                        .param("blogId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCommentRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(commentResponseDTO.getId()))
                .andExpect(jsonPath("$.comment").value(commentResponseDTO.getComment()))
                .andExpect(jsonPath("$.userId").value(commentResponseDTO.getUserId()))
                .andExpect(jsonPath("$.blogId").value(commentResponseDTO.getBlogId()));
    }

    @Test
    void testFindCommentById() throws Exception {
        Mockito.when(commentService.findCommentById(1L)).thenReturn(commentResponseDTO);

        mockMvc.perform(get("/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentResponseDTO.getId()))
                .andExpect(jsonPath("$.comment").value(commentResponseDTO.getComment()))
                .andExpect(jsonPath("$.userId").value(commentResponseDTO.getUserId()))
                .andExpect(jsonPath("$.blogId").value(commentResponseDTO.getBlogId()));
    }

    @Test
    void testFindAllComments() throws Exception {
        Mockito.when(commentService.findAllComments()).thenReturn(List.of(commentResponseDTO));

        mockMvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(commentResponseDTO.getId()))
                .andExpect(jsonPath("$[0].comment").value(commentResponseDTO.getComment()));
    }

    @Test
    void testDeleteCommentById() throws Exception {
        Mockito.doNothing().when(commentService).deleteCommentById(1L);

        mockMvc.perform(delete("/comments/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateCommentById() throws Exception {
        Mockito.when(commentService.updateCommentById(eq(1L), any(CreateCommentRequestDTO.class))).thenReturn(comment);

        mockMvc.perform(put("/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCommentRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentResponseDTO.getId()))
                .andExpect(jsonPath("$.comment").value(commentResponseDTO.getComment()))
                .andExpect(jsonPath("$.userId").value(commentResponseDTO.getUserId()))
                .andExpect(jsonPath("$.blogId").value(commentResponseDTO.getBlogId()));
    }
}
