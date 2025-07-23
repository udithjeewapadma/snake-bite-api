//package com.example.snake_bite_api.controller;
//
//import com.example.snake_bite_api.controller.dto.request.CreateFirstAidRequestDTO;
//import com.example.snake_bite_api.controller.dto.response.FirstAidResponseDTO;
//import com.example.snake_bite_api.models.Admin;
//import com.example.snake_bite_api.models.FirstAid;
//import com.example.snake_bite_api.models.Symptom;
//import com.example.snake_bite_api.service.FirstAidService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(FirstAidController.class)
//public class FirstAidControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private FirstAidService firstAidService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private FirstAidResponseDTO getSampleResponseDTO() {
//        FirstAidResponseDTO dto = new FirstAidResponseDTO();
//        dto.setId(1L);
//        dto.setName("Apply pressure");
//        dto.setDescription("Apply pressure to the bite area");
//        dto.setSymptomId(10L);
//        dto.setAdminId(100L);
//        return dto;
//    }
//
//    private CreateFirstAidRequestDTO getSampleRequestDTO() {
//        CreateFirstAidRequestDTO dto = new CreateFirstAidRequestDTO();
//        dto.setName("Apply pressure");
//        dto.setDescription("Apply pressure to the bite area");
//        return dto;
//    }
//
//    private FirstAid buildFirstAidFromResponseDTO(FirstAidResponseDTO dto) {
//        FirstAid firstAid = new FirstAid();
//        firstAid.setId(dto.getId());
//        firstAid.setName(dto.getName());
//        firstAid.setDescription(dto.getDescription());
//
//        Symptom symptom = new Symptom();
//        symptom.setId(dto.getSymptomId());
//        firstAid.setSymptom(symptom);
//
//        Admin admin = new Admin();
//        admin.setId(dto.getAdminId());
//        firstAid.setAdmin(admin);
//
//        return firstAid;
//    }
//
//    @Test
//    void testCreateFirstAid() throws Exception {
//        FirstAidResponseDTO responseDTO = getSampleResponseDTO();
//
//        Mockito.when(firstAidService.createFirstAid(eq(100L), eq(10L), any(CreateFirstAidRequestDTO.class)))
//                .thenReturn(buildFirstAidFromResponseDTO(responseDTO));
//
//        mockMvc.perform(post("/aids")
//                        .param("adminId", "100")
//                        .param("symptomId", "10")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(getSampleRequestDTO())))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(responseDTO.getId()))
//                .andExpect(jsonPath("$.name").value(responseDTO.getName()))
//                .andExpect(jsonPath("$.description").value(responseDTO.getDescription()))
//                .andExpect(jsonPath("$.symptomId").value(responseDTO.getSymptomId()))
//                .andExpect(jsonPath("$.adminId").value(responseDTO.getAdminId()));
//    }
//
//    @Test
//    void testFindFirstAidById() throws Exception {
//        FirstAidResponseDTO responseDTO = getSampleResponseDTO();
//
//        Mockito.when(firstAidService.findFirstAidById(1L))
//                .thenReturn(responseDTO);
//
//        mockMvc.perform(get("/aids/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value(responseDTO.getName()));
//    }
//
//    @Test
//    void testFindAllFirstAids() throws Exception {
//        FirstAidResponseDTO responseDTO = getSampleResponseDTO();
//
//        Mockito.when(firstAidService.findAllFirstAids())
//                .thenReturn(List.of(responseDTO));
//
//        mockMvc.perform(get("/aids"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name").value(responseDTO.getName()));
//    }
//
//    @Test
//    void testDeleteFirstAidById() throws Exception {
//        mockMvc.perform(delete("/aids/1"))
//                .andExpect(status().isOk());
//
//        Mockito.verify(firstAidService).deleteFirstAidById(1L);
//    }
//
//    @Test
//    void testUpdateFirstAidById() throws Exception {
//        FirstAidResponseDTO responseDTO = getSampleResponseDTO();
//        responseDTO.setName("Updated Name");
//
//        Mockito.when(firstAidService.updateFirstAidById(eq(1L), any(CreateFirstAidRequestDTO.class)))
//                .thenReturn(buildFirstAidFromResponseDTO(responseDTO));
//
//        mockMvc.perform(put("/aids/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(getSampleRequestDTO())))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Updated Name"));
//    }
//}
