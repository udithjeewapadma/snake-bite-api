package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateAdminRequestDTO;
import com.example.snake_bite_api.controller.dto.response.AdminInteractionRequestedSnakeResponseDTO;
import com.example.snake_bite_api.controller.dto.response.AdminResponseDTO;
import com.example.snake_bite_api.models.Admin;
import com.example.snake_bite_api.models.SnakeRequestStatus;
import com.example.snake_bite_api.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateAdmin() throws Exception {
        CreateAdminRequestDTO request = new CreateAdminRequestDTO();
        request.setAdminName("Alice");
        request.setEmail("alice@example.com");
        request.setPhoneNumber("0771234567");

        Admin admin = new Admin();
        admin.setId(1L);
        admin.setAdminName("Alice");
        admin.setEmail("alice@example.com");
        admin.setPhoneNumber("0771234567");

        Mockito.when(adminService.createAdmin(any(CreateAdminRequestDTO.class))).thenReturn(admin);

        mockMvc.perform(post("/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.adminName").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("0771234567"));
    }

    @Test
    void testGetAdminById() throws Exception {
        AdminResponseDTO response = new AdminResponseDTO();
        response.setId(1L);
        response.setAdminName("Bob");
        response.setEmail("bob@example.com");
        response.setPhoneNumber("0778888888");

        Mockito.when(adminService.findAdminById(1L)).thenReturn(response);

        mockMvc.perform(get("/admins/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adminName").value("Bob"))
                .andExpect(jsonPath("$.email").value("bob@example.com"));
    }

    @Test
    void testGetAllAdmins() throws Exception {
        AdminResponseDTO a1 = new AdminResponseDTO();
        a1.setId(1L);
        a1.setAdminName("Admin1");
        a1.setEmail("a1@example.com");
        a1.setPhoneNumber("0700000001");

        AdminResponseDTO a2 = new AdminResponseDTO();
        a2.setId(2L);
        a2.setAdminName("Admin2");
        a2.setEmail("a2@example.com");
        a2.setPhoneNumber("0700000002");

        Mockito.when(adminService.findAllAdmins()).thenReturn(List.of(a1, a2));

        mockMvc.perform(get("/admins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].adminName").value("Admin1"));
    }


    @Test
    void testDeleteAdmin() throws Exception {
        mockMvc.perform(delete("/admins/1"))
                .andExpect(status().isOk());

        Mockito.verify(adminService).deleteAdminById(1L);
    }

    @Test
    void testUpdateAdmin() throws Exception {
        CreateAdminRequestDTO update = new CreateAdminRequestDTO();
        update.setAdminName("Updated");
        update.setEmail("updated@example.com");
        update.setPhoneNumber("0712345678");

        Admin updated = new Admin();
        updated.setId(1L);
        updated.setAdminName("Updated");
        updated.setEmail("updated@example.com");
        updated.setPhoneNumber("0712345678");

        Mockito.when(adminService.updateAdminById(eq(1L), any(CreateAdminRequestDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/admins/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adminName").value("Updated"));
    }

    @Test
    void testApproveRequest() throws Exception {
        AdminInteractionRequestedSnakeResponseDTO dto = new AdminInteractionRequestedSnakeResponseDTO();
        dto.setStatus(SnakeRequestStatus.APPROVED);

        Mockito.when(adminService.approveRequest(1L, 2L)).thenReturn(dto);

        mockMvc.perform(post("/admins/approve")
                        .param("adminId", "1")
                        .param("requestSnakeId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }


    @Test
    void testRejectRequest() throws Exception {
        AdminInteractionRequestedSnakeResponseDTO dto = new AdminInteractionRequestedSnakeResponseDTO();
        dto.setStatus(SnakeRequestStatus.REJECTED);

        Mockito.when(adminService.rejectRequest(1L, 2L)).thenReturn(dto);

        mockMvc.perform(post("/admins/reject")
                        .param("adminId", "1")
                        .param("requestSnakeId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }

}
