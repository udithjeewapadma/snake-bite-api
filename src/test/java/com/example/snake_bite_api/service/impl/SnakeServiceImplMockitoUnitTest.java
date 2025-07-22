package com.example.snake_bite_api.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.example.snake_bite_api.controller.dto.request.CreateSnakeRequestDTO;
import com.example.snake_bite_api.controller.dto.response.SnakeResponseDTO;
import com.example.snake_bite_api.exception.AdminNotFoundException;
import com.example.snake_bite_api.exception.SnakeNotFoundException;
import com.example.snake_bite_api.models.Admin;
import com.example.snake_bite_api.models.Snake;
import com.example.snake_bite_api.models.Venomous;
import com.example.snake_bite_api.repository.AdminRepository;
import com.example.snake_bite_api.repository.SnakeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SnakeServiceImplMockitoUnitTest {

    @InjectMocks
    private SnakeServiceImpl snakeService;

    @Mock
    private SnakeRepository snakeRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(cloudinary.uploader()).thenReturn(uploader);
    }

    @Test
    void testCreateSnake_success() throws IOException {
        Long adminId = 1L;

        Admin admin = new Admin();
        admin.setId(adminId);

        CreateSnakeRequestDTO dto = new CreateSnakeRequestDTO();
        dto.setName("Cobra");
        dto.setColor("Black");
        dto.setSpecies("Naja");
        dto.setPattern("Striped");
        dto.setAverageLength(1.5);
        dto.setImageFiles(Collections.emptyList());

        when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));

        Snake savedSnake = new Snake();
        savedSnake.setId(1L);
        savedSnake.setName("Cobra");
        savedSnake.setColor("Black");
        savedSnake.setSpecies("Naja");
        savedSnake.setPattern("Striped");
        savedSnake.setAverageLength(1.5);
        savedSnake.setVenomous(Venomous.NON_VENOMOUS);
        savedSnake.setAdmin(admin);
        savedSnake.setImageUrl(Collections.emptyList());

        when(snakeRepository.save(any(Snake.class))).thenReturn(savedSnake);

        SnakeResponseDTO response = snakeService.createSnake(adminId, dto);

        assertEquals("Cobra", response.getName());
        assertEquals("Black", response.getColor());
        assertEquals(adminId, response.getAdminId());
    }

    @Test
    void testCreateSnake_adminNotFound() {
        Long adminId = 99L;
        when(adminRepository.findById(adminId)).thenReturn(Optional.empty());
        CreateSnakeRequestDTO dto = new CreateSnakeRequestDTO();

        assertThrows(AdminNotFoundException.class, () -> {
            snakeService.createSnake(adminId, dto);
        });
    }

    @Test
    void testFindSnakeById_success() {
        Snake snake = new Snake();
        snake.setId(1L);
        snake.setName("Python");
        snake.setColor("Brown");
        snake.setSpecies("Pythonidae");
        snake.setPattern("Spotted");
        snake.setAverageLength(2.5);
        snake.setVenomous(Venomous.NON_VENOMOUS);
        snake.setImageUrl(Collections.emptyList());

        Admin admin = new Admin();
        admin.setId(10L);
        snake.setAdmin(admin);

        when(snakeRepository.findById(1L)).thenReturn(Optional.of(snake));

        SnakeResponseDTO dto = snakeService.findSnakeById(1L);

        assertEquals("Python", dto.getName());
        assertEquals(10L, dto.getAdminId());
    }

    @Test
    void testFindSnakeById_notFound() {
        when(snakeRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(SnakeNotFoundException.class, () -> {
            snakeService.findSnakeById(100L);
        });
    }

    @Test
    void testFindAllSnakes() {
        Snake snake1 = new Snake();
        snake1.setId(1L);
        snake1.setName("Krait");
        snake1.setColor("Black");
        snake1.setSpecies("Bungarus");
        snake1.setPattern("Bands");
        snake1.setAverageLength(1.2);
        snake1.setVenomous(Venomous.NON_VENOMOUS);
        Admin admin1 = new Admin(); admin1.setId(101L);
        snake1.setAdmin(admin1);
        snake1.setImageUrl(Collections.emptyList());

        when(snakeRepository.findAll()).thenReturn(List.of(snake1));

        List<SnakeResponseDTO> list = snakeService.findAllSnake();

        assertEquals(1, list.size());
        assertEquals("Krait", list.get(0).getName());
    }

    @Test
    void testUpdateSnakeById_success() throws IOException {
        Snake existing = new Snake();
        existing.setId(1L);
        existing.setName("OldName");
        existing.setColor("Green");
        existing.setSpecies("OldSpecies");
        existing.setPattern("OldPattern");
        existing.setAverageLength(1.1);
        existing.setVenomous(Venomous.NON_VENOMOUS);
        existing.setImageUrl(Collections.emptyList());

        Admin admin = new Admin();
        admin.setId(10L);
        existing.setAdmin(admin);

        CreateSnakeRequestDTO dto = new CreateSnakeRequestDTO();
        dto.setName("UpdatedName");
        dto.setColor("Blue");
        dto.setSpecies("UpdatedSpecies");
        dto.setPattern("UpdatedPattern");
        dto.setAverageLength(2.3);
        dto.setVenomous(Venomous.DEADLY);
        dto.setImageFiles(Collections.emptyList());

        when(snakeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(snakeRepository.save(any(Snake.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SnakeResponseDTO result = snakeService.updateSnakeById(1L, dto);

        assertEquals("UpdatedName", result.getName());
        assertEquals("Blue", result.getColor());
        assertEquals(Venomous.DEADLY, result.getVenomous());
    }

    @Test
    void testUpdateSnakeById_notFound() {
        when(snakeRepository.findById(200L)).thenReturn(Optional.empty());
        CreateSnakeRequestDTO dto = new CreateSnakeRequestDTO();
        assertThrows(SnakeNotFoundException.class, () -> {
            snakeService.updateSnakeById(200L, dto);
        });
    }

    @Test
    void testDeleteSnakeById_success() {
        when(snakeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(snakeRepository).deleteById(1L);
        assertDoesNotThrow(() -> snakeService.deleteSnakeById(1L));
    }

    @Test
    void testDeleteSnakeById_notFound() {
        when(snakeRepository.existsById(999L)).thenReturn(false);
        assertThrows(SnakeNotFoundException.class, () -> {
            snakeService.deleteSnakeById(999L);
        });
    }
}
