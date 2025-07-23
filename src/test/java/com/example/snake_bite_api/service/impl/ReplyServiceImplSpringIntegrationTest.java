package com.example.snake_bite_api.service.impl;

import com.example.snake_bite_api.controller.dto.request.CreateReplyRequestDTO;
import com.example.snake_bite_api.controller.dto.response.ReplyResponseDTO;
import com.example.snake_bite_api.exception.CommentNotFoundException;
import com.example.snake_bite_api.exception.ReplyNotFoundException;
import com.example.snake_bite_api.exception.UserNotFoundException;
import com.example.snake_bite_api.models.Comment;
import com.example.snake_bite_api.models.Reply;
import com.example.snake_bite_api.models.User;
import com.example.snake_bite_api.repository.CommentRepository;
import com.example.snake_bite_api.repository.ReplyRepository;
import com.example.snake_bite_api.repository.UserRepository;
import com.example.snake_bite_api.service.ReplyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReplyServiceImplSpringIntegrationTest {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReplyRepository replyRepository;

    private User user;
    private Comment comment;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserName("testUser");
        user = userRepository.save(user);

        comment = new Comment();
        comment.setCommentText("Test comment");
        comment.setUser(user);
        comment = commentRepository.save(comment);
    }

    @Test
    void createReply_Success() throws UserNotFoundException, CommentNotFoundException {
        CreateReplyRequestDTO dto = new CreateReplyRequestDTO();
        dto.setReplyBody("This is a reply");

        Reply savedReply = replyService.createReply(user.getId(), comment.getId(), dto);

        assertNotNull(savedReply);
        assertEquals("This is a reply", savedReply.getReplyBody());
        assertEquals(comment.getId(), savedReply.getComment().getId());
        assertEquals(user.getId(), savedReply.getUser().getId());
    }

    @Test
    void findReplyById_Success() throws Exception {
        Reply reply = new Reply();
        reply.setReplyBody("Find me");
        reply.setUser(user);
        reply.setComment(comment);
        reply = replyRepository.save(reply);

        ReplyResponseDTO found = replyService.findReplyById(reply.getId());

        assertNotNull(found);
        assertEquals("Find me", found.getReplyBody());
        assertEquals(user.getId(), found.getUserId());
        assertEquals(comment.getId(), found.getCommentId());
    }

    @Test
    void findAllReplies_ReturnsList() throws Exception {
        Reply reply1 = new Reply();
        reply1.setReplyBody("Reply One");
        reply1.setUser(user);
        reply1.setComment(comment);

        Reply reply2 = new Reply();
        reply2.setReplyBody("Reply Two");
        reply2.setUser(user);
        reply2.setComment(comment);

        replyRepository.save(reply1);
        replyRepository.save(reply2);

        List<ReplyResponseDTO> replies = replyService.findAllReplies();

        assertEquals(4, replies.size());
    }

    @Test
    void updateReplyById_Success() throws Exception {
        Reply reply = new Reply();
        reply.setReplyBody("Old reply");
        reply.setUser(user);
        reply.setComment(comment);
        reply = replyRepository.save(reply);

        CreateReplyRequestDTO dto = new CreateReplyRequestDTO();
        dto.setReplyBody("Updated reply");

        Reply updated = replyService.updateReplyById(reply.getId(), dto);

        assertNotNull(updated);
        assertEquals("Updated reply", updated.getReplyBody());
    }

    @Test
    void deleteReplyById_Success() throws Exception {
        Reply reply = new Reply();
        reply.setReplyBody("To be deleted");
        reply.setUser(user);
        reply.setComment(comment);
        reply = replyRepository.save(reply);

        replyService.deleteReplyById(reply.getId());

        assertFalse(replyRepository.existsById(reply.getId()));
    }

    @Test
    void findReplyById_NotFound() {
        assertThrows(ReplyNotFoundException.class, () -> {
            replyService.findReplyById(999L);
        });
    }

    @Test
    void updateReplyById_NotFound() {
        CreateReplyRequestDTO dto = new CreateReplyRequestDTO();
        dto.setReplyBody("Will fail");
        assertThrows(ReplyNotFoundException.class, () -> {
            replyService.updateReplyById(999L, dto);
        });
    }

    @Test
    void deleteReplyById_NotFound() {
        assertThrows(ReplyNotFoundException.class, () -> {
            replyService.deleteReplyById(999L);
        });
    }
}
