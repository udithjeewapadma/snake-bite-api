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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Override
    public Reply createReply(Long userId, Long commentId, CreateReplyRequestDTO createReplyRequestDTO) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + commentId + " not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        Reply reply = new Reply();
        reply.setReplyBody(createReplyRequestDTO.getReplyBody());
        reply.setComment(comment);
        reply.setUser(user);
        return replyRepository.save(reply);
    }

    @Override
    public ReplyResponseDTO findReplyById(Long id) {

        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new ReplyNotFoundException("Reply with id " + id + " not found"));
        ReplyResponseDTO replyResponseDTO = new ReplyResponseDTO();
        replyResponseDTO.setId(reply.getId());
        replyResponseDTO.setReplyBody(reply.getReplyBody());
        replyResponseDTO.setCommentId(reply.getComment().getId());
        replyResponseDTO.setUserId(reply.getUser().getId());
        return replyResponseDTO;
    }

    @Override
    public List<ReplyResponseDTO> findAllReplies() {
        List<Reply> replies = replyRepository.findAll();
        return replies.stream().map(reply -> {
            ReplyResponseDTO replyResponseDTO = new ReplyResponseDTO();
            replyResponseDTO.setId(reply.getId());
            replyResponseDTO.setReplyBody(reply.getReplyBody());
            replyResponseDTO.setCommentId(reply.getComment().getId());
            replyResponseDTO.setUserId(reply.getUser().getId());
            return replyResponseDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteReplyById(Long id) {
        if(!replyRepository.existsById(id)) {
            throw new ReplyNotFoundException("Reply with id " + id + " not found");
        }
        replyRepository.deleteById(id);
    }
}
