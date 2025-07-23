package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateSymptomRequestDTO;
import com.example.snake_bite_api.controller.dto.response.SnakeDTO;
import com.example.snake_bite_api.controller.dto.response.SymptomResponseDTO;
import com.example.snake_bite_api.models.Admin;
import com.example.snake_bite_api.models.Symptom;
import com.example.snake_bite_api.models.Snake;
import com.example.snake_bite_api.service.SymptomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SymptomController.class)
public class SymptomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SymptomService symptomService;

    @Autowired
    private ObjectMapper objectMapper;

    private SymptomResponseDTO getSampleResponseDTO() {
        SymptomResponseDTO dto = new SymptomResponseDTO();
        dto.setId(1L);
        dto.setName("Swelling");
        dto.setDescription("Swelling around the bite area");
        dto.setAdminId(100L);

        SnakeDTO snakeDTO = new SnakeDTO();
        snakeDTO.setId(10L);
        snakeDTO.setSnakeName("Cobra");

        dto.setSnakeList(List.of(snakeDTO));
        return dto;
    }

    private CreateSymptomRequestDTO getSampleRequestDTO() {
        CreateSymptomRequestDTO dto = new CreateSymptomRequestDTO();
        dto.setName("Swelling");
        dto.setDescription("Swelling around the bite area");
        return dto;
    }

    @Test
    void testCreateSymptom() throws Exception {
        SymptomResponseDTO responseDTO = getSampleResponseDTO();

        Mockito.when(symptomService.createSymptom(eq(100L), any(CreateSymptomRequestDTO.class)))
                .thenReturn(buildSymptomFromResponseDTO(responseDTO));

        mockMvc.perform(post("/symptoms")
                        .param("adminId", "100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getSampleRequestDTO())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDTO.getId()))
                .andExpect(jsonPath("$.name").value(responseDTO.getName()))
                .andExpect(jsonPath("$.description").value(responseDTO.getDescription()))
                .andExpect(jsonPath("$.adminId").value(responseDTO.getAdminId()))
                .andExpect(jsonPath("$.snakeList[0].id").value(10))
                .andExpect(jsonPath("$.snakeList[0].snakeName").value("Cobra"));
    }

    @Test
    void testFindSymptomById() throws Exception {
        SymptomResponseDTO responseDTO = getSampleResponseDTO();

        Mockito.when(symptomService.findSymptomById(1L))
                .thenReturn(responseDTO);

        mockMvc.perform(get("/symptoms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Swelling"));
    }

    @Test
    void testFindAllSymptoms() throws Exception {
        SymptomResponseDTO responseDTO = getSampleResponseDTO();

        Mockito.when(symptomService.findAllSymptoms())
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/symptoms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Swelling"));
    }

    @Test
    void testDeleteSymptomById() throws Exception {
        mockMvc.perform(delete("/symptoms/1"))
                .andExpect(status().isOk());

        Mockito.verify(symptomService).deleteSymptomById(1L);
    }

    @Test
    void testUpdateSymptomById() throws Exception {
        SymptomResponseDTO responseDTO = getSampleResponseDTO();
        responseDTO.setName("Updated Swelling");

        Mockito.when(symptomService.updateSymptomById(eq(1L), any(CreateSymptomRequestDTO.class)))
                .thenReturn(buildSymptomFromResponseDTO(responseDTO));

        mockMvc.perform(put("/symptoms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getSampleRequestDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Swelling"));
    }

    // Helper method to convert response DTO to Symptom model, because your service returns Symptom
    private Symptom buildSymptomFromResponseDTO(SymptomResponseDTO dto) {
        Symptom symptom = new Symptom();
        symptom.setId(dto.getId());
        symptom.setName(dto.getName());
        symptom.setDescription(dto.getDescription());

        Snake snake = new Snake();
        snake.setId(dto.getSnakeList().get(0).getId());
        snake.setName(dto.getSnakeList().get(0).getSnakeName());
        symptom.setSnakes(List.of(snake));

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());
        symptom.setAdmin(admin);

        return symptom;
    }
}
