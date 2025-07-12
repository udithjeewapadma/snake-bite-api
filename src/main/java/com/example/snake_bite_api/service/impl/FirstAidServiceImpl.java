package com.example.snake_bite_api.service.impl;

import com.example.snake_bite_api.controller.dto.request.CreateFirstAidRequestDTO;
import com.example.snake_bite_api.exception.AdminNotFoundException;
import com.example.snake_bite_api.exception.SymptomNotFoundException;
import com.example.snake_bite_api.models.Admin;
import com.example.snake_bite_api.models.FirstAid;
import com.example.snake_bite_api.models.Symptom;
import com.example.snake_bite_api.repository.AdminRepository;
import com.example.snake_bite_api.repository.FirstAidRepository;
import com.example.snake_bite_api.repository.SymptomRepository;
import com.example.snake_bite_api.service.FirstAidService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FirstAidServiceImpl implements FirstAidService {

    private final FirstAidRepository firstAidRepository;
    private final SymptomRepository symptomRepository;
    private final AdminRepository adminRepository;

    @Override
    public FirstAid createFirstAid(Long adminId, Long symptomId, CreateFirstAidRequestDTO createFirstAidRequestDTO)
            throws AdminNotFoundException, SymptomNotFoundException {

        Symptom symptom = symptomRepository.findById(symptomId)
                .orElseThrow(() -> new SymptomNotFoundException("Symptom with id " + symptomId + " not found"));

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException("Admin with id " + adminId + " not found"));

        FirstAid firstAid = new FirstAid();
        firstAid.setName(createFirstAidRequestDTO.getName());
        firstAid.setDescription(createFirstAidRequestDTO.getDescription());
        firstAid.setSymptom(symptom);
        firstAid.setAdmin(admin);

        return firstAidRepository.save(firstAid);
    }






}
