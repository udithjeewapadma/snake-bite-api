package com.example.snake_bite_api.service;

import com.example.snake_bite_api.controller.dto.request.CreateSymptomRequestDTO;
import com.example.snake_bite_api.controller.dto.response.SymptomResponseDTO;
import com.example.snake_bite_api.exception.AdminNotFoundException;
import com.example.snake_bite_api.exception.SnakeNotFoundException;
import com.example.snake_bite_api.exception.SymptomNotFoundException;
import com.example.snake_bite_api.models.Symptom;

import java.util.List;

public interface SymptomService {

    Symptom createSymptom(Long adminId,CreateSymptomRequestDTO createSymptomRequestDTO)
            throws SnakeNotFoundException, AdminNotFoundException;

    SymptomResponseDTO findSymptomById(Long id) throws SymptomNotFoundException;

    List<SymptomResponseDTO> findAllSymptoms();

    void deleteSymptomById(Long id) throws SymptomNotFoundException;

    Symptom updateSymptomById(Long id, CreateSymptomRequestDTO createSymptomRequestDTO)
            throws SymptomNotFoundException;

}
