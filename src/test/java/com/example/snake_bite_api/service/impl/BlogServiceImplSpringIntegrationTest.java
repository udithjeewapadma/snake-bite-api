package com.example.snake_bite_api.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.example.snake_bite_api.controller.dto.request.CreateBlogRequestDTO;
import com.example.snake_bite_api.controller.dto.response.BlogResponseDTO;
import com.example.snake_bite_api.exception.UserNotFoundException;
import com.example.snake_bite_api.models.User;
import com.example.snake_bite_api.repository.BlogRepository;
import com.example.snake_bite_api.repository.UserRepository;
import com.example.snake_bite_api.service.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class BlogServiceImplSpringIntegrationTest {

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogRepository blogRepository;

    @MockBean
    private Cloudinary cloudinary;

    @MockBean
    private Uploader uploader;

    private User savedUser;

    @BeforeEach
    void setup() throws IOException {
        User user = new User();
        user.setUserName("integrationUser");
        savedUser = userRepository.save(user);

        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), anyMap()))
                .thenReturn(Map.of("url", "http://image.url/integration.jpg"));
    }

    @Test
    void createBlog_WithImageFiles_Success() throws IOException, UserNotFoundException {
        CreateBlogRequestDTO dto = new CreateBlogRequestDTO();
        dto.setTitle("Integration Test Blog");
        dto.setContent("Content for integration test");

        MockMultipartFile mockFile = new MockMultipartFile(
                "imageFiles",
                "test.jpg",
                "image/jpeg",
                "dummy image content".getBytes());

        dto.setImageFiles(java.util.List.of(mockFile));

        BlogResponseDTO response = blogService.createBlog(savedUser.getId(), dto);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(dto.getTitle());
        assertThat(response.getContent()).isEqualTo(dto.getContent());
        assertThat(response.getUserId()).isEqualTo(savedUser.getId());
        assertThat(response.getImageUrls()).containsExactly("http://image.url/integration.jpg");

        var savedBlog = blogRepository.findById(response.getId()).orElse(null);
        assertThat(savedBlog).isNotNull();
        assertThat(savedBlog.getTitle()).isEqualTo(dto.getTitle());
        assertThat(savedBlog.getImageUrl()).containsExactly("http://image.url/integration.jpg");
    }

    @Test
    void createBlog_UserNotFound_Throws() {
        CreateBlogRequestDTO dto = new CreateBlogRequestDTO();

        Long invalidUserId = 9999L;

        assertThrows(UserNotFoundException.class, () -> blogService.createBlog(invalidUserId, dto));
    }
}
