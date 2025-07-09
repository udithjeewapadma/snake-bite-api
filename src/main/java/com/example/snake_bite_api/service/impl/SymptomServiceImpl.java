package com.example.snake_bite_api.service.impl;

import com.example.snake_bite_api.controller.dto.request.CreateSymptomRequestDTO;
import com.example.snake_bite_api.controller.dto.response.SnakeDTO;
import com.example.snake_bite_api.controller.dto.response.SymptomResponseDTO;
import com.example.snake_bite_api.exception.SnakeNotFoundException;
import com.example.snake_bite_api.exception.SymptomNotFoundException;
import com.example.snake_bite_api.models.Snake;
import com.example.snake_bite_api.models.Symptom;
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


    @Override
    public Symptom createSymptom(CreateSymptomRequestDTO createSymptomRequestDTO) throws SnakeNotFoundException {

        List<Snake> snakes = snakeRepository.findAllById(createSymptomRequestDTO.getSnakeIds());

        if (snakes.isEmpty() || snakes.size() != createSymptomRequestDTO.getSnakeIds().size()) {
            throw new SnakeNotFoundException("Some snake IDs are invalid");
        }

        Symptom symptom = new Symptom();
        symptom.setName(createSymptomRequestDTO.getName());
        symptom.setDescription(createSymptomRequestDTO.getDescription());

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
}
