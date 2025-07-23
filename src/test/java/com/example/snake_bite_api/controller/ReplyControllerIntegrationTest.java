package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateReplyRequestDTO;
import com.example.snake_bite_api.controller.dto.request.CreateUserRequestDTO;
import com.example.snake_bite_api.controller.dto.request.CreateCommentRequestDTO;
import com.example.snake_bite_api.controller.dto.response.ReplyResponseDTO;
import com.example.snake_bite_api.controller.dto.response.UserResponseDTO;
import com.example.snake_bite_api.controller.dto.response.CommentResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReplyControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testReplyCRUDOperations() {
        // 1. Create User
        CreateUserRequestDTO userDTO = new CreateUserRequestDTO();
        userDTO.setUserName("replyUser");
        userDTO.setEmail("replyuser@example.com");
        userDTO.setPhoneNumber("5551234567");

        ResponseEntity<UserResponseDTO> userResp = restTemplate.postForEntity("/users", userDTO, UserResponseDTO.class);
        assertThat(userResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long userId = userResp.getBody().getId();

        // 2. Create Comment (for replying)
        CreateCommentRequestDTO commentDTO = new CreateCommentRequestDTO();
        commentDTO.setCommentText("Comment to reply to");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateCommentRequestDTO> commentRequest = new HttpEntity<>(commentDTO, headers);

        ResponseEntity<CommentResponseDTO> commentResp = restTemplate.postForEntity(
                "/comments?userId=" + userId + "&blogId=1",  // Assuming blogId 1 exists or you can create a blog before this test
                commentRequest,
                CommentResponseDTO.class
        );
        assertThat(commentResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long commentId = commentResp.getBody().getId();

        // 3. Create Reply
        CreateReplyRequestDTO replyDTO = new CreateReplyRequestDTO();
        replyDTO.setReplyBody("This is a test reply");

        HttpEntity<CreateReplyRequestDTO> replyRequest = new HttpEntity<>(replyDTO, headers);

        ResponseEntity<ReplyResponseDTO> createReplyResp = restTemplate.postForEntity(
                "/replies?userId=" + userId + "&commentId=" + commentId,
                replyRequest,
                ReplyResponseDTO.class
        );
        assertThat(createReplyResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ReplyResponseDTO createdReply = createReplyResp.getBody();
        assertThat(createdReply.getReplyBody()).isEqualTo("This is a test reply");
        Long replyId = createdReply.getId();

        // 4. Get Reply by Id
        ResponseEntity<ReplyResponseDTO> getReplyResp = restTemplate.getForEntity("/replies/" + replyId, ReplyResponseDTO.class);
        assertThat(getReplyResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getReplyResp.getBody().getId()).isEqualTo(replyId);

        // 5. Update Reply
        CreateReplyRequestDTO updateDTO = new CreateReplyRequestDTO();
        updateDTO.setReplyBody("Updated reply body");

        HttpEntity<CreateReplyRequestDTO> updateRequest = new HttpEntity<>(updateDTO, headers);

        ResponseEntity<ReplyResponseDTO> updateResp = restTemplate.exchange(
                "/replies/" + replyId,
                HttpMethod.PUT,
                updateRequest,
                ReplyResponseDTO.class
        );
        assertThat(updateResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResp.getBody().getReplyBody()).isEqualTo("Updated reply body");

        // 6. Get All Replies
        ResponseEntity<List> allRepliesResp = restTemplate.getForEntity("/replies", List.class);
        assertThat(allRepliesResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(allRepliesResp.getBody()).isNotEmpty();

        // 7. Delete Reply
        ResponseEntity<Void> deleteResp = restTemplate.exchange(
                "/replies/" + replyId,
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertThat(deleteResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 8. Confirm Deletion
        ResponseEntity<ReplyResponseDTO> getDeletedResp = restTemplate.getForEntity("/replies/" + replyId, ReplyResponseDTO.class);
        assertThat(getDeletedResp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
