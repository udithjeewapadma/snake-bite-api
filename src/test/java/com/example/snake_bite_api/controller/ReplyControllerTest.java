//package com.example.snake_bite_api.controller;
//
//import com.example.snake_bite_api.controller.dto.request.CreateReplyRequestDTO;
//import com.example.snake_bite_api.controller.dto.response.ReplyResponseDTO;
//import com.example.snake_bite_api.models.Comment;
//import com.example.snake_bite_api.models.Reply;
//import com.example.snake_bite_api.models.User;
//import com.example.snake_bite_api.service.ReplyService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Arrays;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ReplyController.class)
//public class ReplyControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ReplyService replyService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private Reply createSampleReply() {
//        Reply reply = new Reply();
//        reply.setId(1L);
//        reply.setReplyBody("Sample reply");
//
//        User user = new User();
//        user.setId(2L);
//        reply.setUser(user);
//
//        Comment comment = new Comment();
//        comment.setId(3L);
//        reply.setComment(comment);
//
//        return reply;
//    }
//
//    private ReplyResponseDTO createSampleReplyResponseDTO() {
//        ReplyResponseDTO dto = new ReplyResponseDTO();
//        dto.setId(1L);
//        dto.setReplyBody("Sample reply");
//        dto.setUserId(2L);
//        dto.setCommentId(3L);
//        return dto;
//    }
//
//    @Test
//    void testCreateReply() throws Exception {
//        CreateReplyRequestDTO requestDTO = new CreateReplyRequestDTO();
//        requestDTO.setReplyBody("Sample reply");
//
//        Reply reply = createSampleReply();
//        Mockito.when(replyService.createReply(eq(2L), eq(3L), any(CreateReplyRequestDTO.class)))
//                .thenReturn(reply);
//
//        mockMvc.perform(post("/replies")
//                        .param("userId", "2")
//                        .param("commentId", "3")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.replyBody").value("Sample reply"))
//                .andExpect(jsonPath("$.userId").value(2))
//                .andExpect(jsonPath("$.commentId").value(3));
//    }
//
//    @Test
//    void testGetReplyById() throws Exception {
//        ReplyResponseDTO dto = createSampleReplyResponseDTO();
//        Mockito.when(replyService.findReplyById(1L)).thenReturn(dto);
//
//        mockMvc.perform(get("/replies/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.replyBody").value("Sample reply"))
//                .andExpect(jsonPath("$.userId").value(2))
//                .andExpect(jsonPath("$.commentId").value(3));
//    }
//
//    @Test
//    void testGetAllReplies() throws Exception {
//        ReplyResponseDTO dto1 = createSampleReplyResponseDTO();
//        Mockito.when(replyService.findAllReplies()).thenReturn(Arrays.asList(dto1));
//
//        mockMvc.perform(get("/replies"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(1))
//                .andExpect(jsonPath("$[0].replyBody").value("Sample reply"));
//    }
//
//    @Test
//    void testDeleteReply() throws Exception {
//        mockMvc.perform(delete("/replies/1"))
//                .andExpect(status().isOk());
//
//        Mockito.verify(replyService).deleteReplyById(1L);
//    }
//
//    @Test
//    void testUpdateReply() throws Exception {
//        CreateReplyRequestDTO requestDTO = new CreateReplyRequestDTO();
//        requestDTO.setReplyBody("Updated reply");
//
//        Reply updatedReply = createSampleReply();
//        updatedReply.setReplyBody("Updated reply");
//
//        Mockito.when(replyService.updateReplyById(eq(1L), any(CreateReplyRequestDTO.class)))
//                .thenReturn(updatedReply);
//
//        mockMvc.perform(put("/replies/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.replyBody").value("Updated reply"))
//                .andExpect(jsonPath("$.userId").value(2))
//                .andExpect(jsonPath("$.commentId").value(3));
//    }
//}
