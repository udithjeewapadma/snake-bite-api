package com.example.snake_bite_api.service.impl;

import com.example.snake_bite_api.controller.dto.request.CreateReplyRequestDTO;
import com.example.snake_bite_api.exception.CommentNotFoundException;
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
}
