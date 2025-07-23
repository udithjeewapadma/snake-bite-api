package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateNewSnakeRequestDTO;
import com.example.snake_bite_api.controller.dto.response.NewSnakeResponseDTO;
import com.example.snake_bite_api.models.SnakeRequestStatus;
import com.example.snake_bite_api.models.Venomous;
import com.example.snake_bite_api.service.RequestedNewSnakeService;
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

@WebMvcTest(RequestedNewSnakeController.class)
public class RequestedNewSnakeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestedNewSnakeService requestedNewSnakeService;

    private NewSnakeResponseDTO getSampleResponseDTO() {
        NewSnakeResponseDTO dto = new NewSnakeResponseDTO();
        dto.setId(1L);
        dto.setName("New Cobra");
        dto.setSpecies("Naja naja");
        dto.setColor("Brown");
        dto.setPattern("Striped");
        dto.setAverageLength(1.5);
        dto.setVenomous(Venomous.HIGHLY_VENOMOUS);
        dto.setImageUrls(List.of("http://image.url/new-snake.jpg"));
        dto.setStatus(SnakeRequestStatus.PENDING);
        dto.setUserId(5L);
        return dto;
    }

    @Test
    void testCreateRequest() throws Exception {
        NewSnakeResponseDTO responseDTO = getSampleResponseDTO();

        Mockito.when(requestedNewSnakeService.createRequest(eq(5L), any(CreateNewSnakeRequestDTO.class)))
                .thenReturn(responseDTO);

        MockMultipartFile image = new MockMultipartFile(
                "image", "new-snake.jpg", MediaType.IMAGE_JPEG_VALUE, "dummy-image".getBytes());

        mockMvc.perform(multipart("/newSnakes")
                        .file(image)
                        .param("name", "New Cobra")
                        .param("species", "Naja naja")
                        .param("color", "Brown")
                        .param("pattern", "Striped")
                        .param("averageLength", "1.5")
                        .param("venomous", "HIGHLY_VENOMOUS")
                        .param("userId", "5"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Cobra"))
                .andExpect(jsonPath("$.species").value("Naja naja"))
                .andExpect(jsonPath("$.color").value("Brown"))
                .andExpect(jsonPath("$.pattern").value("Striped"))
                .andExpect(jsonPath("$.averageLength").value(1.5))
                .andExpect(jsonPath("$.venomous").value("HIGHLY_VENOMOUS"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.userId").value(5))
                .andExpect(jsonPath("$.imageUrls[0]").value("http://image.url/new-snake.jpg"));
    }

    @Test
    void testFindAllRequests() throws Exception {
        NewSnakeResponseDTO dto = getSampleResponseDTO();

        Mockito.when(requestedNewSnakeService.findAllRequests())
                .thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/newSnakes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("New Cobra"))
                .andExpect(jsonPath("$[0].venomous").value("HIGHLY_VENOMOUS"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }
}
