package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateBlogRequestDTO;
import com.example.snake_bite_api.controller.dto.response.BlogResponseDTO;
import com.example.snake_bite_api.exception.BlogNotFoundException;
import com.example.snake_bite_api.service.BlogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlogController.class)
public class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogService blogService;

    @Autowired
    private ObjectMapper objectMapper;

    private BlogResponseDTO blog1;
    private BlogResponseDTO blog2;
    private CreateBlogRequestDTO createBlogRequestDTO;

    @BeforeEach
    void setup() {
        blog1 = new BlogResponseDTO();
        blog1.setId(1L);
        blog1.setTitle("First Blog");
        blog1.setContent("Content of first blog");

        blog2 = new BlogResponseDTO();
        blog2.setId(2L);
        blog2.setTitle("Second Blog");
        blog2.setContent("Content of second blog");

        createBlogRequestDTO = new CreateBlogRequestDTO();
        createBlogRequestDTO.setTitle("New Blog");
        createBlogRequestDTO.setContent("New blog content");
        // Set other fields if any
    }

    @Test
    void testCreateBlog() throws Exception {
        Mockito.when(blogService.createBlog(eq(1L), any(CreateBlogRequestDTO.class))).thenReturn(blog1);

        mockMvc.perform(multipart("/blogs")
                        .param("title", "New Blog")
                        .param("content", "New blog content")
                        .param("userId", "1")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(blog1.getId()))
                .andExpect(jsonPath("$.title").value(blog1.getTitle()))
                .andExpect(jsonPath("$.content").value(blog1.getContent()));
    }


    @Test
    void testFindBlogById() throws Exception {
        Mockito.when(blogService.findBlogById(1L)).thenReturn(blog1);

        mockMvc.perform(get("/blogs/{blog-id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(blog1.getId()))
                .andExpect(jsonPath("$.title").value(blog1.getTitle()))
                .andExpect(jsonPath("$.content").value(blog1.getContent()));
    }

    @Test
    void testFindAllBlogs() throws Exception {
        Mockito.when(blogService.findAllBlogs()).thenReturn(List.of(blog1, blog2));

        mockMvc.perform(get("/blogs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is(blog1.getTitle())))
                .andExpect(jsonPath("$[1].title", is(blog2.getTitle())));
    }

    @Test
    void testDeleteBlog() throws Exception {
        Mockito.doNothing().when(blogService).deleteBlogById(1L);

        mockMvc.perform(delete("/blogs/{blog-id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateBlog() throws Exception {
        Mockito.when(blogService.updateBlogById(eq(1L), any(CreateBlogRequestDTO.class))).thenReturn(blog2);

        mockMvc.perform(multipart("/blogs/{blog-id}", 1L)
                        .param("title", "Updated Blog")
                        .param("content", "Updated content")
                        .with(request -> {
                            request.setMethod("PUT");  // multipart defaults to POST, override to PUT
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(blog2.getId()))
                .andExpect(jsonPath("$.title").value(blog2.getTitle()))
                .andExpect(jsonPath("$.content").value(blog2.getContent()));
    }

}
