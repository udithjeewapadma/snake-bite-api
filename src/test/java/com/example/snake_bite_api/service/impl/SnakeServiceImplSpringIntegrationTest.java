package com.example.snake_bite_api.service.impl;

import com.example.snake_bite_api.controller.dto.request.CreateSnakeRequestDTO;
import com.example.snake_bite_api.controller.dto.response.SnakeResponseDTO;
import com.example.snake_bite_api.exception.SnakeNotFoundException;
import com.example.snake_bite_api.models.Admin;
import com.example.snake_bite_api.repository.AdminRepository;
import com.example.snake_bite_api.repository.SnakeRepository;
import com.example.snake_bite_api.service.SnakeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class SnakeServiceImplSpringIntegrationTest {

    @Autowired
    private SnakeService snakeService;

    @Autowired
    private SnakeRepository snakeRepository;

    @Autowired
    private AdminRepository adminRepository;

    private Long adminId;

    @BeforeEach
    void setup() {
        Admin admin = new Admin();
        admin.setAdminName("Test Admin");
        admin.setEmail("test@admin.com");
        admin.setPhoneNumber("0771234567");
        admin = adminRepository.save(admin);
        adminId = admin.getId();
    }

    @Test
    void testCreateAndFindSnake_success() throws Exception {
        CreateSnakeRequestDTO dto = new CreateSnakeRequestDTO();
        dto.setName("Python");
        dto.setColor("Brown");
        dto.setSpecies("Pythonidae");
        dto.setPattern("Spotted");
        dto.setAverageLength(2.5);
        dto.setImageFiles(Collections.emptyList());

        SnakeResponseDTO response = snakeService.createSnake(adminId, dto);

        assertNotNull(response.getId());
        assertEquals("Python", response.getName());

        SnakeResponseDTO fetched = snakeService.findSnakeById(response.getId());
        assertEquals(response.getId(), fetched.getId());
    }

    @Test
    void testUpdateSnake_success() throws Exception {
        CreateSnakeRequestDTO createDto = new CreateSnakeRequestDTO();
        createDto.setName("Krait");
        createDto.setColor("Black");
        createDto.setSpecies("Bungarus");
        createDto.setPattern("Striped");
        createDto.setAverageLength(1.2);
        createDto.setImageFiles(Collections.emptyList());

        SnakeResponseDTO created = snakeService.createSnake(adminId, createDto);

        CreateSnakeRequestDTO updateDto = new CreateSnakeRequestDTO();
        updateDto.setName("Updated Krait");
        updateDto.setColor("Dark Black");
        updateDto.setPattern("Solid");
        updateDto.setAverageLength(1.5);

        SnakeResponseDTO updated = snakeService.updateSnakeById(created.getId(), updateDto);

        assertEquals("Updated Krait", updated.getName());
        assertEquals("Dark Black", updated.getColor());
    }

    @Test
    void testDeleteSnake_success() throws Exception {
        CreateSnakeRequestDTO dto = new CreateSnakeRequestDTO();
        dto.setName("Viper");
        dto.setColor("Green");
        dto.setSpecies("Viperidae");
        dto.setPattern("Spotted");
        dto.setAverageLength(1.8);
        dto.setImageFiles(Collections.emptyList());

        SnakeResponseDTO created = snakeService.createSnake(adminId, dto);

        snakeService.deleteSnakeById(created.getId());

        assertThrows(SnakeNotFoundException.class, () -> {
            snakeService.findSnakeById(created.getId());
        });
    }
}
