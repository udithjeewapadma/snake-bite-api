package com.example.snake_bite_api.service.impl;

import com.example.snake_bite_api.controller.dto.request.CreateSymptomRequestDTO;
import com.example.snake_bite_api.exception.AdminNotFoundException;
import com.example.snake_bite_api.exception.SnakeNotFoundException;
import com.example.snake_bite_api.exception.SymptomNotFoundException;
import com.example.snake_bite_api.models.Admin;
import com.example.snake_bite_api.models.Snake;
import com.example.snake_bite_api.models.Symptom;
import com.example.snake_bite_api.repository.AdminRepository;
import com.example.snake_bite_api.repository.SnakeRepository;
import com.example.snake_bite_api.repository.SymptomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SymptomServiceImplMockitoUnitTest {

    @InjectMocks
    private SymptomServiceImpl symptomService;

    @Mock
    private SymptomRepository symptomRepository;

    @Mock
    private SnakeRepository snakeRepository;

    @Mock
    private AdminRepository adminRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createSymptom_success() throws SnakeNotFoundException, AdminNotFoundException {
        Long adminId = 1L;
        List<Long> snakeIds = List.of(10L, 20L);

        CreateSymptomRequestDTO dto = new CreateSymptomRequestDTO();
        dto.setName("Swelling");
        dto.setDescription("Severe swelling in affected area");
        dto.setSnakeIds(snakeIds);

        Admin admin = new Admin();
        admin.setId(adminId);

        Snake snake1 = new Snake();
        snake1.setId(10L);
        Snake snake2 = new Snake();
        snake2.setId(20L);

        when(snakeRepository.findAllById(snakeIds)).thenReturn(List.of(snake1, snake2));
        when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));
        when(symptomRepository.save(any(Symptom.class))).thenAnswer(i -> i.getArguments()[0]);

        Symptom result = symptomService.createSymptom(adminId, dto);

        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(admin, result.getAdmin());
        assertEquals(2, result.getSnakes().size());

        verify(snakeRepository).findAllById(snakeIds);
        verify(adminRepository).findById(adminId);
        verify(symptomRepository).save(any(Symptom.class));
    }

    @Test
    void createSymptom_throwsSnakeNotFoundException() {
        CreateSymptomRequestDTO dto = new CreateSymptomRequestDTO();
        dto.setSnakeIds(List.of(1L, 2L));

        when(snakeRepository.findAllById(dto.getSnakeIds())).thenReturn(Collections.emptyList());

        assertThrows(SnakeNotFoundException.class, () -> symptomService.createSymptom(1L, dto));
    }

    @Test
    void createSymptom_throwsAdminNotFoundException() {
        List<Long> snakeIds = List.of(10L);
        CreateSymptomRequestDTO dto = new CreateSymptomRequestDTO();
        dto.setSnakeIds(snakeIds);

        Snake snake = new Snake();
        snake.setId(10L);

        when(snakeRepository.findAllById(snakeIds)).thenReturn(List.of(snake));
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AdminNotFoundException.class, () -> symptomService.createSymptom(1L, dto));
    }

    @Test
    void findSymptomById_success() throws SymptomNotFoundException {
        Long symptomId = 100L;

        Symptom symptom = new Symptom();
        symptom.setId(symptomId);
        symptom.setName("Pain");
        symptom.setDescription("Sharp pain");
        Admin admin = new Admin();
        admin.setId(5L);
        symptom.setAdmin(admin);
        symptom.setSnakes(List.of(new Snake(){{
            setId(1L);
            setName("Cobra");
        }}));

        when(symptomRepository.findById(symptomId)).thenReturn(Optional.of(symptom));

        var dto = symptomService.findSymptomById(symptomId);

        assertEquals(symptomId, dto.getId());
        assertEquals("Pain", dto.getName());
        assertEquals("Sharp pain", dto.getDescription());
        assertEquals(5L, dto.getAdminId());
        assertFalse(dto.getSnakeList().isEmpty());
        assertEquals("Cobra", dto.getSnakeList().get(0).getSnakeName());
    }

    @Test
    void findSymptomById_throwsSymptomNotFoundException() {
        when(symptomRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(SymptomNotFoundException.class, () -> symptomService.findSymptomById(1L));
    }

    @Test
    void deleteSymptomById_success() throws SymptomNotFoundException {
        Long id = 1L;
        when(symptomRepository.existsById(id)).thenReturn(true);

        symptomService.deleteSymptomById(id);

        verify(symptomRepository).deleteById(id);
    }

    @Test
    void deleteSymptomById_throwsSymptomNotFoundException() {
        when(symptomRepository.existsById(1L)).thenReturn(false);
        assertThrows(SymptomNotFoundException.class, () -> symptomService.deleteSymptomById(1L));
    }

    @Test
    void updateSymptomById_success() throws SymptomNotFoundException, SnakeNotFoundException {
        Long id = 1L;
        List<Long> snakeIds = List.of(10L);

        CreateSymptomRequestDTO dto = new CreateSymptomRequestDTO();
        dto.setName("Updated Name");
        dto.setDescription("Updated Description");
        dto.setSnakeIds(snakeIds);

        Symptom existingSymptom = new Symptom();
        existingSymptom.setId(id);

        Snake snake = new Snake();
        snake.setId(10L);

        when(symptomRepository.findById(id)).thenReturn(Optional.of(existingSymptom));
        when(snakeRepository.findAllById(snakeIds)).thenReturn(List.of(snake));
        when(symptomRepository.save(existingSymptom)).thenReturn(existingSymptom);

        Symptom updated = symptomService.updateSymptomById(id, dto);

        assertEquals("Updated Name", updated.getName());
        assertEquals("Updated Description", updated.getDescription());
        assertEquals(1, updated.getSnakes().size());
    }

    @Test
    void updateSymptomById_throwsSymptomNotFoundException() {
        when(symptomRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(SymptomNotFoundException.class, () ->
                symptomService.updateSymptomById(1L, new CreateSymptomRequestDTO()));
    }

    @Test
    void updateSymptomById_throwsSnakeNotFoundException() {
        Long id = 1L;
        CreateSymptomRequestDTO dto = new CreateSymptomRequestDTO();
        dto.setSnakeIds(List.of(1L));

        Symptom existingSymptom = new Symptom();
        existingSymptom.setId(id);

        when(symptomRepository.findById(id)).thenReturn(Optional.of(existingSymptom));
        when(snakeRepository.findAllById(dto.getSnakeIds())).thenReturn(Collections.emptyList());

        assertThrows(SnakeNotFoundException.class, () -> symptomService.updateSymptomById(id, dto));
    }
}
