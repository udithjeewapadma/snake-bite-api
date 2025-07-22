package com.example.snake_bite_api.service.impl;

import com.example.snake_bite_api.controller.dto.request.CreateFirstAidRequestDTO;
import com.example.snake_bite_api.controller.dto.response.FirstAidResponseDTO;
import com.example.snake_bite_api.exception.AdminNotFoundException;
import com.example.snake_bite_api.exception.FirstAidNotFoundException;
import com.example.snake_bite_api.exception.SymptomNotFoundException;
import com.example.snake_bite_api.models.Admin;
import com.example.snake_bite_api.models.FirstAid;
import com.example.snake_bite_api.models.Symptom;
import com.example.snake_bite_api.repository.AdminRepository;
import com.example.snake_bite_api.repository.FirstAidRepository;
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
public class FirstAidServiceImplSpringIntegrationTest {

    @Autowired
    private FirstAidServiceImpl firstAidService;

    @Autowired
    private FirstAidRepository firstAidRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private SymptomRepository symptomRepository;

    private Admin admin;
    private Symptom symptom;

    @BeforeAll
    void setup() {
        admin = new Admin();
        admin.setAdminName("Test Admin");
        admin = adminRepository.save(admin);

        symptom = new Symptom();
        symptom.setName("Test Symptom");
        symptom.setDescription("Test Description");
        symptom.setAdmin(admin);
        symptom = symptomRepository.save(symptom);
    }

    @Test
    void createFirstAid_shouldCreateAndReturn() throws AdminNotFoundException, SymptomNotFoundException {
        CreateFirstAidRequestDTO dto = new CreateFirstAidRequestDTO();
        dto.setName("Apply bandage");
        dto.setDescription("Wrap with bandage");

        FirstAid firstAid = firstAidService.createFirstAid(admin.getId(), symptom.getId(), dto);

        assertNotNull(firstAid.getId());
        assertEquals(dto.getName(), firstAid.getName());
        assertEquals(dto.getDescription(), firstAid.getDescription());
        assertEquals(admin.getId(), firstAid.getAdmin().getId());
        assertEquals(symptom.getId(), firstAid.getSymptom().getId());
    }

    @Test
    void findFirstAidById_shouldReturnCorrect() throws FirstAidNotFoundException, AdminNotFoundException, SymptomNotFoundException {
        CreateFirstAidRequestDTO dto = new CreateFirstAidRequestDTO();
        dto.setName("Cool compress");
        dto.setDescription("Use a cold compress");

        FirstAid created = firstAidService.createFirstAid(admin.getId(), symptom.getId(), dto);

        FirstAidResponseDTO responseDTO = firstAidService.findFirstAidById(created.getId());

        assertEquals(created.getId(), responseDTO.getId());
        assertEquals(dto.getName(), responseDTO.getName());
        assertEquals(dto.getDescription(), responseDTO.getDescription());
        assertEquals(admin.getId(), responseDTO.getAdminId());
        assertEquals(symptom.getId(), responseDTO.getSymptomId());
    }

    @Test
    void findAllFirstAids_shouldReturnList() throws AdminNotFoundException, SymptomNotFoundException {
        CreateFirstAidRequestDTO dto1 = new CreateFirstAidRequestDTO();
        dto1.setName("Method 1");
        dto1.setDescription("Description 1");

        CreateFirstAidRequestDTO dto2 = new CreateFirstAidRequestDTO();
        dto2.setName("Method 2");
        dto2.setDescription("Description 2");

        firstAidService.createFirstAid(admin.getId(), symptom.getId(), dto1);
        firstAidService.createFirstAid(admin.getId(), symptom.getId(), dto2);

        List<FirstAidResponseDTO> list = firstAidService.findAllFirstAids();

        assertTrue(list.size() >= 2);
    }

    @Test
    void deleteFirstAidById_shouldDelete() throws FirstAidNotFoundException, AdminNotFoundException, SymptomNotFoundException {
        CreateFirstAidRequestDTO dto = new CreateFirstAidRequestDTO();
        dto.setName("Delete test");
        dto.setDescription("To delete");

        FirstAid firstAid = firstAidService.createFirstAid(admin.getId(), symptom.getId(), dto);
        Long id = firstAid.getId();

        firstAidService.deleteFirstAidById(id);

        assertFalse(firstAidRepository.existsById(id));
    }

    @Test
    void updateFirstAidById_shouldUpdate() throws FirstAidNotFoundException, AdminNotFoundException, SymptomNotFoundException {
        CreateFirstAidRequestDTO dto = new CreateFirstAidRequestDTO();
        dto.setName("Old Name");
        dto.setDescription("Old Desc");

        FirstAid firstAid = firstAidService.createFirstAid(admin.getId(), symptom.getId(), dto);

        CreateFirstAidRequestDTO updateDTO = new CreateFirstAidRequestDTO();
        updateDTO.setName("New Name");
        updateDTO.setDescription("New Desc");

        FirstAid updated = firstAidService.updateFirstAidById(firstAid.getId(), updateDTO);

        assertEquals("New Name", updated.getName());
        assertEquals("New Desc", updated.getDescription());
    }
}
