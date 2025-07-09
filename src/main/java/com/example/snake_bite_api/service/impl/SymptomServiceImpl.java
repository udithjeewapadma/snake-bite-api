package com.example.snake_bite_api.service.impl;

import com.example.snake_bite_api.controller.dto.request.CreateSymptomRequestDTO;
import com.example.snake_bite_api.controller.dto.response.SnakeDTO;
import com.example.snake_bite_api.controller.dto.response.SymptomResponseDTO;
import com.example.snake_bite_api.exception.AdminNotFoundException;
import com.example.snake_bite_api.exception.SnakeNotFoundException;
import com.example.snake_bite_api.exception.SymptomNotFoundException;
import com.example.snake_bite_api.models.Admin;
import com.example.snake_bite_api.models.Snake;
import com.example.snake_bite_api.models.Symptom;
import com.example.snake_bite_api.repository.AdminRepository;
import com.example.snake_bite_api.repository.SnakeRepository;
import com.example.snake_bite_api.repository.SymptomRepository;
import com.example.snake_bite_api.service.SymptomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SymptomServiceImpl implements SymptomService {

    @Autowired
    private SymptomRepository symptomRepository;

    @Autowired
    private SnakeRepository snakeRepository;

    @Autowired
    private AdminRepository adminRepository;


    @Override
    public Symptom createSymptom(Long adminId,CreateSymptomRequestDTO createSymptomRequestDTO)
            throws SnakeNotFoundException, AdminNotFoundException {


        List<Snake> snakes = snakeRepository.findAllById(createSymptomRequestDTO.getSnakeIds());

        if (snakes.isEmpty() || snakes.size() != createSymptomRequestDTO.getSnakeIds().size()) {
            throw new SnakeNotFoundException("Some snake IDs are invalid");
        }

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException("Admin with id " + adminId + " not found"));

        Symptom symptom = new Symptom();
        symptom.setName(createSymptomRequestDTO.getName());
        symptom.setDescription(createSymptomRequestDTO.getDescription());
        symptom.setAdmin(admin);

        symptom.setSnakes(new ArrayList<>(snakes));
        return symptomRepository.save(symptom);
    }

    @Override
    public SymptomResponseDTO findSymptomById(Long id) throws SymptomNotFoundException {

        Symptom symptom = symptomRepository.findById(id)
                .orElseThrow(() -> new SymptomNotFoundException("Symptom with id " + id + " not found"));

        SymptomResponseDTO symptomResponseDTO = new SymptomResponseDTO();
        symptomResponseDTO.setId(symptom.getId());
        symptomResponseDTO.setName(symptom.getName());
        symptomResponseDTO.setDescription(symptom.getDescription());
        symptomResponseDTO.setAdminId(symptom.getAdmin().getId());

        List<SnakeDTO> snakeDTOS = symptom.getSnakes().stream().map(snake -> {
            SnakeDTO snakeDTO = new SnakeDTO();
            snakeDTO.setId(snake.getId());
            snakeDTO.setSnakeName(snake.getName());
            return snakeDTO;
        }).toList();
        symptomResponseDTO.setSnakeList(snakeDTOS);
        return symptomResponseDTO;
    }

    @Override
    public List<SymptomResponseDTO> findAllSymptoms() {
        List<Symptom> symptoms = symptomRepository.findAll();
        return symptoms.stream().map(symptom -> {
            SymptomResponseDTO symptomResponseDTO = new SymptomResponseDTO();
            symptomResponseDTO.setId(symptom.getId());
            symptomResponseDTO.setName(symptom.getName());
            symptomResponseDTO.setDescription(symptom.getDescription());
            symptomResponseDTO.setAdminId(symptom.getAdmin().getId());

            List<SnakeDTO> snakeDTOS = symptom.getSnakes().stream().map(snake -> {
                SnakeDTO snakeDTO = new SnakeDTO();
                snakeDTO.setId(snake.getId());
                snakeDTO.setSnakeName(snake.getName());
                return snakeDTO;
            }).toList();
            symptomResponseDTO.setSnakeList(snakeDTOS);
            return symptomResponseDTO;

        }).collect(Collectors.toList());
    }

    @Override
    public void deleteSymptomById(Long id) throws SymptomNotFoundException {
        if (!symptomRepository.existsById(id)) {
            throw new SymptomNotFoundException("Symptom with id " + id + " not found");
        }
        symptomRepository.deleteById(id);
    }

    @Override
    public Symptom updateSymptomById(Long id, CreateSymptomRequestDTO createSymptomRequestDTO)
            throws SymptomNotFoundException, SnakeNotFoundException {

        Symptom existingSymptom = symptomRepository.findById(id)
                .orElseThrow(() -> new SymptomNotFoundException("Symptom with id " + id + " not found"));

        List<Snake> snakes = snakeRepository.findAllById(createSymptomRequestDTO.getSnakeIds());
        if (snakes.isEmpty() || snakes.size() != createSymptomRequestDTO.getSnakeIds().size()) {
            throw new SnakeNotFoundException("Some snake IDs are invalid");
        }

        existingSymptom.setName(createSymptomRequestDTO.getName());
        existingSymptom.setDescription(createSymptomRequestDTO.getDescription());
        existingSymptom.setSnakes(new ArrayList<>(snakes));

        return symptomRepository.save(existingSymptom);
    }

}
