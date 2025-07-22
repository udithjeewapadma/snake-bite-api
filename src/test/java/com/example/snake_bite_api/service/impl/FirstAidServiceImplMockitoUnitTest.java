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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FirstAidServiceImplMockitoUnitTest {

    @InjectMocks
    private FirstAidServiceImpl firstAidService;

    @Mock
    private FirstAidRepository firstAidRepository;

    @Mock
    private SymptomRepository symptomRepository;

    @Mock
    private AdminRepository adminRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createFirstAid_success() throws AdminNotFoundException, SymptomNotFoundException {
        Long adminId = 1L;
        Long symptomId = 10L;

        CreateFirstAidRequestDTO dto = new CreateFirstAidRequestDTO();
        dto.setName("Apply ice");
        dto.setDescription("Apply ice to reduce swelling");

        Admin admin = new Admin();
        admin.setId(adminId);

        Symptom symptom = new Symptom();
        symptom.setId(symptomId);

        FirstAid savedFirstAid = new FirstAid();
        savedFirstAid.setId(100L);
        savedFirstAid.setName(dto.getName());
        savedFirstAid.setDescription(dto.getDescription());
        savedFirstAid.setAdmin(admin);
        savedFirstAid.setSymptom(symptom);

        when(symptomRepository.findById(symptomId)).thenReturn(Optional.of(symptom));
        when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));
        when(firstAidRepository.save(any(FirstAid.class))).thenReturn(savedFirstAid);

        FirstAid result = firstAidService.createFirstAid(adminId, symptomId, dto);

        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(admin, result.getAdmin());
        assertEquals(symptom, result.getSymptom());

        verify(symptomRepository).findById(symptomId);
        verify(adminRepository).findById(adminId);
        verify(firstAidRepository).save(any(FirstAid.class));
    }

    @Test
    void createFirstAid_throwsSymptomNotFoundException() {
        when(symptomRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(SymptomNotFoundException.class, () ->
                firstAidService.createFirstAid(1L, 1L, new CreateFirstAidRequestDTO())
        );
    }

    @Test
    void createFirstAid_throwsAdminNotFoundException() {
        Symptom symptom = new Symptom();
        symptom.setId(1L);
        when(symptomRepository.findById(1L)).thenReturn(Optional.of(symptom));
        when(adminRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(AdminNotFoundException.class, () ->
                firstAidService.createFirstAid(1L, 1L, new CreateFirstAidRequestDTO())
        );
    }

    @Test
    void findFirstAidById_success() throws FirstAidNotFoundException {
        FirstAid firstAid = new FirstAid();
        firstAid.setId(1L);
        firstAid.setName("Ice pack");
        firstAid.setDescription("Apply ice pack");
        Admin admin = new Admin();
        admin.setId(10L);
        firstAid.setAdmin(admin);
        Symptom symptom = new Symptom();
        symptom.setId(20L);
        firstAid.setSymptom(symptom);

        when(firstAidRepository.findById(1L)).thenReturn(Optional.of(firstAid));

        FirstAidResponseDTO dto = firstAidService.findFirstAidById(1L);

        assertEquals(1L, dto.getId());
        assertEquals("Ice pack", dto.getName());
        assertEquals("Apply ice pack", dto.getDescription());
        assertEquals(10L, dto.getAdminId());
        assertEquals(20L, dto.getSymptomId());
    }

    @Test
    void findFirstAidById_throwsFirstAidNotFoundException() {
        when(firstAidRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(FirstAidNotFoundException.class, () ->
                firstAidService.findFirstAidById(1L)
        );
    }

    @Test
    void findAllFirstAids_returnsList() {
        FirstAid fa1 = new FirstAid();
        fa1.setId(1L);
        fa1.setName("First Aid 1");
        fa1.setDescription("Desc 1");
        Admin admin = new Admin();
        admin.setId(100L);
        fa1.setAdmin(admin);
        Symptom symptom = new Symptom();
        symptom.setId(200L);
        fa1.setSymptom(symptom);

        when(firstAidRepository.findAll()).thenReturn(List.of(fa1));

        var list = firstAidService.findAllFirstAids();

        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals("First Aid 1", list.get(0).getName());
    }

    @Test
    void deleteFirstAidById_success() throws FirstAidNotFoundException {
        when(firstAidRepository.existsById(1L)).thenReturn(true);

        firstAidService.deleteFirstAidById(1L);

        verify(firstAidRepository).deleteById(1L);
    }

    @Test
    void deleteFirstAidById_throwsFirstAidNotFoundException() {
        when(firstAidRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(FirstAidNotFoundException.class, () ->
                firstAidService.deleteFirstAidById(1L)
        );
    }

    @Test
    void updateFirstAidById_success() throws FirstAidNotFoundException {
        FirstAid existing = new FirstAid();
        existing.setId(1L);
        existing.setName("Old name");
        existing.setDescription("Old desc");

        CreateFirstAidRequestDTO dto = new CreateFirstAidRequestDTO();
        dto.setName("New name");
        dto.setDescription("New desc");

        when(firstAidRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(firstAidRepository.save(existing)).thenReturn(existing);

        FirstAid updated = firstAidService.updateFirstAidById(1L, dto);

        assertEquals("New name", updated.getName());
        assertEquals("New desc", updated.getDescription());
    }

    @Test
    void updateFirstAidById_throwsFirstAidNotFoundException() {
        when(firstAidRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(FirstAidNotFoundException.class, () ->
                firstAidService.updateFirstAidById(1L, new CreateFirstAidRequestDTO())
        );
    }
}
