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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReplyServiceImplMockitoUnitTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReplyRepository replyRepository;

    @InjectMocks
    private ReplyServiceImpl replyService;

    private User user;
    private Comment comment;
    private Reply reply;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        comment = new Comment();
        comment.setId(1L);

        reply = new Reply();
        reply.setId(1L);
        reply.setReplyBody("Test reply");
        reply.setUser(user);
        reply.setComment(comment);
    }

    @Test
    void createReply_Success() throws UserNotFoundException, CommentNotFoundException {
        CreateReplyRequestDTO dto = new CreateReplyRequestDTO();
        dto.setReplyBody("New reply");

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(replyRepository.save(any(Reply.class))).thenReturn(reply);

        Reply createdReply = replyService.createReply(1L, 1L, dto);

        assertNotNull(createdReply);
        assertEquals("Test reply", createdReply.getReplyBody());
        verify(replyRepository, times(1)).save(any(Reply.class));
    }

    @Test
    void createReply_CommentNotFound() {
        CreateReplyRequestDTO dto = new CreateReplyRequestDTO();
        dto.setReplyBody("New reply");

        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> replyService.createReply(1L, 1L, dto));
    }

    @Test
    void createReply_UserNotFound() {
        CreateReplyRequestDTO dto = new CreateReplyRequestDTO();
        dto.setReplyBody("New reply");

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> replyService.createReply(1L, 1L, dto));
    }

    @Test
    void findReplyById_Success() throws ReplyNotFoundException {
        when(replyRepository.findById(1L)).thenReturn(Optional.of(reply));

        ReplyResponseDTO response = replyService.findReplyById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test reply", response.getReplyBody());
    }

    @Test
    void findReplyById_NotFound() {
        when(replyRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ReplyNotFoundException.class, () -> replyService.findReplyById(1L));
    }

    @Test
    void findAllReplies_Success() {
        when(replyRepository.findAll()).thenReturn(Arrays.asList(reply));

        List<ReplyResponseDTO> replies = replyService.findAllReplies();

        assertEquals(1, replies.size());
        assertEquals("Test reply", replies.get(0).getReplyBody());
    }

    @Test
    void deleteReplyById_Success() throws ReplyNotFoundException {
        when(replyRepository.existsById(1L)).thenReturn(true);
        replyService.deleteReplyById(1L);
        verify(replyRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteReplyById_NotFound() {
        when(replyRepository.existsById(1L)).thenReturn(false);
        assertThrows(ReplyNotFoundException.class, () -> replyService.deleteReplyById(1L));
    }

    @Test
    void updateReplyById_Success() throws ReplyNotFoundException {
        CreateReplyRequestDTO dto = new CreateReplyRequestDTO();
        dto.setReplyBody("Updated reply");

        Reply reply = new Reply();
        reply.setId(1L);
        reply.setReplyBody("Old reply");

        when(replyRepository.findById(1L)).thenReturn(Optional.of(reply));
        when(replyRepository.save(any(Reply.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reply updatedReply = replyService.updateReplyById(1L, dto);

        assertNotNull(updatedReply);
        assertEquals("Updated reply", updatedReply.getReplyBody()); // ✅ correct expectation
    }


    @Test
    void updateReplyById_NotFound() {
        CreateReplyRequestDTO dto = new CreateReplyRequestDTO();
        dto.setReplyBody("Updated reply");

        when(replyRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ReplyNotFoundException.class, () -> replyService.updateReplyById(1L, dto));
    }
}
