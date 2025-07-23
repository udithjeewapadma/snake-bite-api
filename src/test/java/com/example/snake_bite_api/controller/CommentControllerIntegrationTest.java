package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateCommentRequestDTO;
import com.example.snake_bite_api.controller.dto.request.CreateBlogRequestDTO;
import com.example.snake_bite_api.controller.dto.request.CreateUserRequestDTO;
import com.example.snake_bite_api.controller.dto.response.CommentResponseDTO;
import com.example.snake_bite_api.controller.dto.response.UserResponseDTO;
import com.example.snake_bite_api.controller.dto.response.BlogResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
     void testCommentCRUDOperations() {

        // 1. Create User (needed for comment)
        CreateUserRequestDTO userDTO = new CreateUserRequestDTO();
        userDTO.setUserName("testuser");
        userDTO.setEmail("testuser@example.com");
        userDTO.setPhoneNumber("1234567890");

        ResponseEntity<UserResponseDTO> userResponse = restTemplate.postForEntity("/users", userDTO, UserResponseDTO.class);
        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long userId = userResponse.getBody().getId();

        // 2. Create Blog (needed for comment)
        CreateBlogRequestDTO blogDTO = new CreateBlogRequestDTO();
        blogDTO.setTitle("Test Blog");
        blogDTO.setContent("Blog Description");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateBlogRequestDTO> blogRequest = new HttpEntity<>(blogDTO, headers);

        ResponseEntity<BlogResponseDTO> blogResponse = restTemplate.postForEntity("/blogs?userId=" + userId, blogRequest, BlogResponseDTO.class);
        assertThat(blogResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Long blogId = blogResponse.getBody().getId();

        // 3. Create Comment
        CreateCommentRequestDTO commentDTO = new CreateCommentRequestDTO();
        commentDTO.setCommentText("This is a test comment.");

        HttpEntity<CreateCommentRequestDTO> commentRequest = new HttpEntity<>(commentDTO, headers);

        ResponseEntity<CommentResponseDTO> createCommentResponse = restTemplate.postForEntity(
                "/comments?userId=" + userId + "&blogId=" + blogId,
                commentRequest,
                CommentResponseDTO.class
        );
        assertThat(createCommentResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        CommentResponseDTO createdComment = createCommentResponse.getBody();
        assertThat(createdComment).isNotNull();
        assertThat(createdComment.getCommentText()).isEqualTo("This is a test comment.");
        Long commentId = createdComment.getId();

        // 4. Get Comment by ID
        ResponseEntity<CommentResponseDTO> getCommentResponse = restTemplate.getForEntity("/comments/" + commentId, CommentResponseDTO.class);
        assertThat(getCommentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getCommentResponse.getBody().getId()).isEqualTo(commentId);

        // 5. Update Comment
        CreateCommentRequestDTO updateCommentDTO = new CreateCommentRequestDTO();
        updateCommentDTO.setCommentText("Updated comment text.");

        HttpEntity<CreateCommentRequestDTO> updateRequest = new HttpEntity<>(updateCommentDTO, headers);

        ResponseEntity<CommentResponseDTO> updateResponse = restTemplate.exchange(
                "/comments/" + commentId,
                HttpMethod.PUT,
                updateRequest,
                CommentResponseDTO.class
        );
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody().getCommentText()).isEqualTo("Updated comment text.");

        // 6. Get All Comments
        ResponseEntity<List> allCommentsResponse = restTemplate.getForEntity("/comments", List.class);
        assertThat(allCommentsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(allCommentsResponse.getBody()).isNotEmpty();

        // 7. Delete Comment
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/comments/" + commentId,
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 8. Verify comment deleted
        ResponseEntity<CommentResponseDTO> getDeletedCommentResponse = restTemplate.getForEntity("/comments/" + commentId, CommentResponseDTO.class);
        assertThat(getDeletedCommentResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
