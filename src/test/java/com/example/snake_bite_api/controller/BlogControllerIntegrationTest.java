package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateUserRequestDTO;
import com.example.snake_bite_api.controller.dto.response.BlogResponseDTO;
import com.example.snake_bite_api.controller.dto.response.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BlogControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final String baseUrl = "/blogs";

    @Test
    void testCreateGetUpdateDeleteBlog(){

        // 1. Create User (required for blog creation)
        CreateUserRequestDTO userDto = new CreateUserRequestDTO();
        userDto.setUserName("testuser");
        userDto.setEmail("testuser@example.com");
        userDto.setPhoneNumber("1234567890");

        ResponseEntity<UserResponseDTO> userResponse = restTemplate.postForEntity(
                "/users", userDto, UserResponseDTO.class);
        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long userId = userResponse.getBody().getId();
        assertThat(userId).isNotNull();

        // 2. Create Blog (multipart/form-data + userId query param)
        MultiValueMap<String, Object> blogBody = new LinkedMultiValueMap<>();
        blogBody.add("title", "My First Blog");
        blogBody.add("description", "This is a description for my blog.");

        // Add image from classpath (adjust path to a real test image in src/test/resources)
        ClassPathResource imageResource = new ClassPathResource("test-image.jpg");
        blogBody.add("image", imageResource);

        HttpHeaders blogHeaders = new HttpHeaders();
        blogHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> blogRequest = new HttpEntity<>(blogBody, blogHeaders);

        ResponseEntity<BlogResponseDTO> createBlogResponse = restTemplate.postForEntity(
                baseUrl + "?userId=" + userId,
                blogRequest,
                BlogResponseDTO.class
        );

        assertThat(createBlogResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        BlogResponseDTO createdBlog = createBlogResponse.getBody();
        assertThat(createdBlog).isNotNull();
        assertThat(createdBlog.getId()).isNotNull();

        Long blogId = createdBlog.getId();

        // 3. Get Blog by ID
        ResponseEntity<BlogResponseDTO> getBlogResponse = restTemplate.getForEntity(
                baseUrl + "/" + blogId,
                BlogResponseDTO.class
        );

        assertThat(getBlogResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getBlogResponse.getBody().getId()).isEqualTo(blogId);

        // 4. Update Blog (multipart/form-data + userId query param if needed)
        MultiValueMap<String, Object> updateBody = new LinkedMultiValueMap<>();
        updateBody.add("title", "Updated Blog Title");
        updateBody.add("description", "Updated blog description.");

        HttpEntity<MultiValueMap<String, Object>> updateRequest = new HttpEntity<>(updateBody, blogHeaders);

        ResponseEntity<BlogResponseDTO> updateResponse = restTemplate.exchange(
                baseUrl + "/" + blogId,
                HttpMethod.PUT,
                updateRequest,
                BlogResponseDTO.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody().getTitle()).isEqualTo("Updated Blog Title");

        // 5. Delete Blog
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/" + blogId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 6. Verify blog is deleted (should return 404)
        ResponseEntity<BlogResponseDTO> getDeletedBlogResponse = restTemplate.getForEntity(
                baseUrl + "/" + blogId,
                BlogResponseDTO.class
        );

        assertThat(getDeletedBlogResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
