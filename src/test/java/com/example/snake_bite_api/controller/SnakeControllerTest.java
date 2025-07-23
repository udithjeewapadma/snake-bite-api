package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateSnakeRequestDTO;
import com.example.snake_bite_api.controller.dto.response.SnakeResponseDTO;
import com.example.snake_bite_api.models.Venomous;
import com.example.snake_bite_api.service.SnakeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SnakeController.class)
public class SnakeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SnakeService snakeService;

    @Autowired
    private ObjectMapper objectMapper;

    private SnakeResponseDTO getSampleResponseDTO() {
        SnakeResponseDTO dto = new SnakeResponseDTO();
        dto.setId(1L);
        dto.setName("Cobra");
        dto.setVenomous(Venomous.HIGHLY_VENOMOUS);
        dto.setImageUrls(List.of("http://image.url/snake.jpg"));
        return dto;
    }

    @Test
    void testCreateSnake() throws Exception {
        SnakeResponseDTO responseDTO = getSampleResponseDTO();

        Mockito.when(snakeService.createSnake(eq(10L), any(CreateSnakeRequestDTO.class)))
                .thenReturn(responseDTO);

        MockMultipartFile image = new MockMultipartFile(
                "image", "snake.jpg", MediaType.IMAGE_JPEG_VALUE, "dummy-image".getBytes());

        mockMvc.perform(multipart("/snakes")
                        .file(image)
                        .param("name", "Cobra")
                        .param("description", "Very venomous snake")
                        .param("venomous", "HIGHLY_VENOMOUS")  // Use enum name here
                        .param("adminId", "10"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Cobra"))
                .andExpect(jsonPath("$.venomous").value("HIGHLY_VENOMOUS"));  // Check enum string in JSON
    }

    @Test
    void testGetSnakeById() throws Exception {
        SnakeResponseDTO responseDTO = getSampleResponseDTO();
        Mockito.when(snakeService.findSnakeById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/snakes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cobra"))
                .andExpect(jsonPath("$.venomous").value("HIGHLY_VENOMOUS"));
    }

    @Test
    void testGetAllSnakes() throws Exception {
        SnakeResponseDTO responseDTO = getSampleResponseDTO();
        Mockito.when(snakeService.findAllSnake()).thenReturn(Collections.singletonList(responseDTO));

        mockMvc.perform(get("/snakes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Cobra"));
    }

    @Test
    void testDeleteSnakeById() throws Exception {
        mockMvc.perform(delete("/snakes/1"))
                .andExpect(status().isOk());

        Mockito.verify(snakeService).deleteSnakeById(1L);
    }

    @Test
    void testUpdateSnake() throws Exception {
        SnakeResponseDTO responseDTO = getSampleResponseDTO();
        responseDTO.setName("Updated Cobra");

        Mockito.when(snakeService.updateSnakeById(eq(1L), any(CreateSnakeRequestDTO.class)))
                .thenReturn(responseDTO);

        MockMultipartFile image = new MockMultipartFile(
                "image", "updated.jpg", MediaType.IMAGE_JPEG_VALUE, "updated-image".getBytes());

        mockMvc.perform(multipart("/snakes/1")
                        .file(image)
                        .with(request -> {
                            request.setMethod("PUT"); // simulate PUT for multipart
                            return request;
                        })
                        .param("name", "Updated Cobra")
                        .param("description", "Updated description")
                        .param("venomous", "HIGHLY_VENOMOUS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Cobra"))
                .andExpect(jsonPath("$.venomous").value("HIGHLY_VENOMOUS"));
    }
}
