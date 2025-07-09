package com.example.snake_bite_api.service;

import com.example.snake_bite_api.controller.dto.request.CreateSymptomRequestDTO;
import com.example.snake_bite_api.controller.dto.response.SymptomResponseDTO;
import com.example.snake_bite_api.exception.SnakeNotFoundException;
import com.example.snake_bite_api.exception.SymptomNotFoundException;
import com.example.snake_bite_api.models.Symptom;

import java.util.List;

public interface SymptomService {

    Symptom createSymptom(CreateSymptomRequestDTO createSymptomRequestDTO) throws SnakeNotFoundException;

    SymptomResponseDTO findSymptomById(Long id) throws SymptomNotFoundException;

    List<SymptomResponseDTO> findAllSymptoms();

    void deleteSymptomById(Long id) throws SymptomNotFoundException;

}
