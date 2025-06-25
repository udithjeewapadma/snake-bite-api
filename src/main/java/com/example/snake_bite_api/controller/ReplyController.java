package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateReplyRequestDTO;
import com.example.snake_bite_api.controller.dto.response.ReplyResponseDTO;
import com.example.snake_bite_api.exception.ReplyNotFoundException;
import com.example.snake_bite_api.models.Reply;
import com.example.snake_bite_api.service.ReplyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/replies")
@AllArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReplyResponseDTO createReply(@RequestParam Long userId,
                                        @RequestParam Long commentId,
                                        @RequestBody CreateReplyRequestDTO createReplyRequestDTO) {
        Reply reply = replyService.createReply(userId, commentId, createReplyRequestDTO);
        ReplyResponseDTO replyResponseDTO = new ReplyResponseDTO();
        replyResponseDTO.setId(reply.getId());
        replyResponseDTO.setReplyBody(reply.getReplyBody());
        replyResponseDTO.setCommentId(reply.getUser().getId());
        replyResponseDTO.setUserId(reply.getUser().getId());
        return replyResponseDTO;
    }

    @GetMapping("/{reply-id}")
    public ReplyResponseDTO getReply(@PathVariable("reply-id") Long replyId) {
        return replyService.findReplyById(replyId);
    }

    @GetMapping
    public List<ReplyResponseDTO> getReplies() {
        return replyService.findAllReplies();
    }

    @DeleteMapping("/{reply-id}")
    public void deleteReply(@PathVariable("reply-id") Long replyId) {
        replyService.deleteReplyById(replyId);
    }

    @PutMapping("/{reply-id}")
    public ReplyResponseDTO updateReply(@PathVariable("reply-id") Long replyId, CreateReplyRequestDTO createReplyRequestDTO){

        Reply reply = replyService.updateReplyById(replyId, createReplyRequestDTO);
        ReplyResponseDTO replyResponseDTO = new ReplyResponseDTO();
        replyResponseDTO.setId(reply.getId());
        replyResponseDTO.setReplyBody(reply.getReplyBody());
        replyResponseDTO.setCommentId(reply.getUser().getId());
        replyResponseDTO.setUserId(reply.getUser().getId());
        return replyResponseDTO;
    }

}
