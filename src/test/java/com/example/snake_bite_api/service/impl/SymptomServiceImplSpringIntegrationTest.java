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
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SymptomServiceImplSpringIntegrationTest {

    @Autowired
    private SymptomServiceImpl symptomService;

    @Autowired
    private SymptomRepository symptomRepository;

    @Autowired
    private SnakeRepository snakeRepository;

    @Autowired
    private AdminRepository adminRepository;

    private Admin admin;
    private Snake snake1;
    private Snake snake2;

    @BeforeAll
    void setup() {
        admin = new Admin();
        admin.setAdminName("Test Admin");
        admin = adminRepository.save(admin);

        snake1 = new Snake();
        snake1.setName("Cobra");
        snake1 = snakeRepository.save(snake1);

        snake2 = new Snake();
        snake2.setName("Krait");
        snake2 = snakeRepository.save(snake2);
    }

    @Test
    void createSymptom_shouldCreateAndReturnSymptom() throws SnakeNotFoundException, AdminNotFoundException {
        CreateSymptomRequestDTO dto = new CreateSymptomRequestDTO();
        dto.setName("Nausea");
        dto.setDescription("Feeling sick");
        dto.setSnakeIds(List.of(snake1.getId(), snake2.getId()));

        Symptom symptom = symptomService.createSymptom(admin.getId(), dto);

        assertNotNull(symptom.getId());
        assertEquals(dto.getName(), symptom.getName());
        assertEquals(dto.getDescription(), symptom.getDescription());
        assertEquals(admin.getId(), symptom.getAdmin().getId());
        assertEquals(2, symptom.getSnakes().size());
    }

    @Test
    void findSymptomById_shouldReturnCorrectSymptom() throws SnakeNotFoundException, AdminNotFoundException, SymptomNotFoundException {
        // create first
        CreateSymptomRequestDTO dto = new CreateSymptomRequestDTO();
        dto.setName("Headache");
        dto.setDescription("Severe headache");
        dto.setSnakeIds(List.of(snake1.getId()));

        Symptom created = symptomService.createSymptom(admin.getId(), dto);

        var responseDTO = symptomService.findSymptomById(created.getId());

        assertEquals("Headache", responseDTO.getName());
        assertEquals("Severe headache", responseDTO.getDescription());
        assertEquals(admin.getId(), responseDTO.getAdminId());
        assertEquals(1, responseDTO.getSnakeList().size());
        assertEquals(snake1.getId(), responseDTO.getSnakeList().get(0).getId());
    }

    @Test
    void findAllSymptoms_shouldReturnList() throws SnakeNotFoundException, AdminNotFoundException {
        // create two symptoms
        CreateSymptomRequestDTO dto1 = new CreateSymptomRequestDTO();
        dto1.setName("Symptom1");
        dto1.setDescription("Desc1");
        dto1.setSnakeIds(List.of(snake1.getId()));

        CreateSymptomRequestDTO dto2 = new CreateSymptomRequestDTO();
        dto2.setName("Symptom2");
        dto2.setDescription("Desc2");
        dto2.setSnakeIds(List.of(snake2.getId()));

        symptomService.createSymptom(admin.getId(), dto1);
        symptomService.createSymptom(admin.getId(), dto2);

        var allSymptoms = symptomService.findAllSymptoms();

        assertTrue(allSymptoms.size() >= 2);
    }

    @Test
    void deleteSymptomById_shouldDelete() throws SnakeNotFoundException, AdminNotFoundException, SymptomNotFoundException {
        CreateSymptomRequestDTO dto = new CreateSymptomRequestDTO();
        dto.setName("ToDelete");
        dto.setDescription("To be deleted");
        dto.setSnakeIds(List.of(snake1.getId()));

        Symptom symptom = symptomService.createSymptom(admin.getId(), dto);
        Long id = symptom.getId();

        symptomService.deleteSymptomById(id);

        assertFalse(symptomRepository.existsById(id));
    }

    @Test
    void updateSymptomById_shouldUpdate() throws SnakeNotFoundException, AdminNotFoundException, SymptomNotFoundException {
        CreateSymptomRequestDTO dto = new CreateSymptomRequestDTO();
        dto.setName("OldName");
        dto.setDescription("OldDesc");
        dto.setSnakeIds(List.of(snake1.getId()));

        Symptom symptom = symptomService.createSymptom(admin.getId(), dto);

        CreateSymptomRequestDTO updateDTO = new CreateSymptomRequestDTO();
        updateDTO.setName("NewName");
        updateDTO.setDescription("NewDesc");
        updateDTO.setSnakeIds(List.of(snake2.getId()));

        Symptom updated = symptomService.updateSymptomById(symptom.getId(), updateDTO);

        assertEquals("NewName", updated.getName());
        assertEquals("NewDesc", updated.getDescription());
        assertEquals(1, updated.getSnakes().size());
        assertEquals(snake2.getId(), updated.getSnakes().get(0).getId());
    }
}
