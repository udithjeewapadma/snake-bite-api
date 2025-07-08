package com.example.snake_bite_api.service;

import com.example.snake_bite_api.controller.dto.request.CreateSymptomRequestDTO;
import com.example.snake_bite_api.exception.SnakeNotFoundException;
import com.example.snake_bite_api.models.Symptom;

public interface SymptomService {

    Symptom createSymptom(CreateSymptomRequestDTO createSymptomRequestDTO) throws SnakeNotFoundException;
}
