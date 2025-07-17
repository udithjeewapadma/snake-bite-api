package com.example.snake_bite_api.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.example.snake_bite_api.controller.dto.request.CreateBlogRequestDTO;
import com.example.snake_bite_api.controller.dto.response.BlogResponseDTO;
import com.example.snake_bite_api.exception.BlogNotFoundException;
import com.example.snake_bite_api.exception.UserNotFoundException;
import com.example.snake_bite_api.models.Blog;
import com.example.snake_bite_api.models.User;
import com.example.snake_bite_api.repository.BlogRepository;
import com.example.snake_bite_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlogServiceImplMockitoUnitTest {

    private BlogRepository blogRepository;
    private UserRepository userRepository;
    private Cloudinary cloudinary;
    private Uploader uploader;  // needed to mock uploader()

    private BlogServiceImpl blogService;

    @BeforeEach
    void setup() {
        blogRepository = mock(BlogRepository.class);
        userRepository = mock(UserRepository.class);
        cloudinary = mock(Cloudinary.class);
        uploader = mock(Uploader.class);

        when(cloudinary.uploader()).thenReturn(uploader);

        blogService = new BlogServiceImpl(blogRepository, userRepository, cloudinary);
    }

    @Test
    void createBlog_Success() throws IOException, UserNotFoundException {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        CreateBlogRequestDTO dto = new CreateBlogRequestDTO();
        dto.setTitle("Test Title");
        dto.setContent("Test Content");

        MultipartFile fileMock = mock(MultipartFile.class);
        dto.setImageFiles(List.of(fileMock));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(fileMock.getBytes()).thenReturn("dummy bytes".getBytes());

        // Mock Cloudinary upload response
        Map<String, Object> uploadResult = Map.of("url", "http://image.url/test.jpg");
        when(uploader.upload(any(byte[].class), any(Map.class))).thenReturn(uploadResult);

        // Capture saved Blog entity
        ArgumentCaptor<Blog> blogCaptor = ArgumentCaptor.forClass(Blog.class);
        when(blogRepository.save(blogCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        BlogResponseDTO response = blogService.createBlog(userId, dto);

        // Assert
        assertNotNull(response);
        assertEquals(dto.getTitle(), response.getTitle());
        assertEquals(dto.getContent(), response.getContent());
        assertEquals(userId, response.getUserId());
        assertThat(response.getImageUrls()).containsExactly("http://image.url/test.jpg");

        Blog savedBlog = blogCaptor.getValue();
        assertEquals(dto.getTitle(), savedBlog.getTitle());
        assertEquals(dto.getContent(), savedBlog.getContent());
        assertEquals(user, savedBlog.getUser());
        assertThat(savedBlog.getImageUrl()).containsExactly("http://image.url/test.jpg");
    }

    @Test
    void createBlog_UserNotFound_ThrowsException() {
        // Arrange
        Long userId = 1L;
        CreateBlogRequestDTO dto = new CreateBlogRequestDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> blogService.createBlog(userId, dto));
    }

    @Test
    void findBlogById_Success() throws BlogNotFoundException {
        // Arrange
        Long blogId = 2L;
        User user = new User();
        user.setId(1L);

        Blog blog = new Blog();
        blog.setId(blogId);
        blog.setTitle("Title");
        blog.setContent("Content");
        blog.setUser(user);
        blog.setImageUrl(List.of("url1", "url2"));

        when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));

        // Act
        BlogResponseDTO response = blogService.findBlogById(blogId);

        // Assert
        assertNotNull(response);
        assertEquals(blogId, response.getId());
        assertEquals(blog.getTitle(), response.getTitle());
        assertEquals(blog.getContent(), response.getContent());
        assertEquals(user.getId(), response.getUserId());
        assertThat(response.getImageUrls()).containsExactlyElementsOf(blog.getImageUrl());
    }

    @Test
    void findBlogById_NotFound_ThrowsException() {
        // Arrange
        Long blogId = 2L;
        when(blogRepository.findById(blogId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BlogNotFoundException.class, () -> blogService.findBlogById(blogId));
    }

    @Test
    void findAllBlogs_ReturnsAll() {
        // Arrange
        User user = new User();
        user.setId(5L);

        Blog blog1 = new Blog();
        blog1.setId(1L);
        blog1.setTitle("T1");
        blog1.setContent("C1");
        blog1.setUser(user);
        blog1.setImageUrl(List.of("url1"));

        Blog blog2 = new Blog();
        blog2.setId(2L);
        blog2.setTitle("T2");
        blog2.setContent("C2");
        blog2.setUser(user);
        blog2.setImageUrl(List.of("url2"));

        when(blogRepository.findAll()).thenReturn(List.of(blog1, blog2));

        // Act
        List<BlogResponseDTO> results = blogService.findAllBlogs();

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getId()).isEqualTo(blog1.getId());
        assertThat(results.get(1).getId()).isEqualTo(blog2.getId());
    }

    @Test
    void deleteBlogById_Success() throws BlogNotFoundException {
        // Arrange
        Long id = 10L;
        when(blogRepository.existsById(id)).thenReturn(true);

        // Act
        blogService.deleteBlogById(id);

        // Assert
        verify(blogRepository).deleteById(id);
    }

    @Test
    void deleteBlogById_NotFound_ThrowsException() {
        // Arrange
        Long id = 10L;
        when(blogRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(BlogNotFoundException.class, () -> blogService.deleteBlogById(id));
    }

    @Test
    void updateBlogById_Success() throws BlogNotFoundException, IOException {
        // Arrange
        Long id = 3L;

        Blog existingBlog = new Blog();
        existingBlog.setId(id);
        existingBlog.setTitle("Old Title");
        existingBlog.setContent("Old Content");
        User user = new User();
        user.setId(7L);
        existingBlog.setUser(user);

        when(blogRepository.findById(id)).thenReturn(Optional.of(existingBlog));

        CreateBlogRequestDTO dto = new CreateBlogRequestDTO();
        dto.setTitle("New Title");
        dto.setContent("New Content");

        MultipartFile fileMock = mock(MultipartFile.class);
        dto.setImageFiles(List.of(fileMock));
        when(fileMock.getBytes()).thenReturn("dummy bytes".getBytes());

        Map<String, Object> uploadResult = Map.of("url", "http://image.url/new.jpg");
        when(uploader.upload(any(byte[].class), any(Map.class))).thenReturn(uploadResult);

        when(blogRepository.save(any(Blog.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        BlogResponseDTO response = blogService.updateBlogById(id, dto);

        // Assert
        assertNotNull(response);
        assertEquals(dto.getTitle(), response.getTitle());
        assertEquals(dto.getContent(), response.getContent());
        assertEquals(user.getId(), response.getUserId());
        assertThat(response.getImageUrls()).containsExactly("http://image.url/new.jpg");
    }

    @Test
    void updateBlogById_NotFound_ThrowsException() {
        // Arrange
        Long id = 3L;
        CreateBlogRequestDTO dto = new CreateBlogRequestDTO();

        when(blogRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BlogNotFoundException.class, () -> blogService.updateBlogById(id, dto));
    }
}
