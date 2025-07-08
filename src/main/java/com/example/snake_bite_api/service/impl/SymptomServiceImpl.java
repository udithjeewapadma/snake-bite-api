package com.example.snake_bite_api.service.impl;

import com.example.snake_bite_api.controller.dto.request.CreateSymptomRequestDTO;
import com.example.snake_bite_api.exception.SnakeNotFoundException;
import com.example.snake_bite_api.models.Snake;
import com.example.snake_bite_api.models.Symptom;
import com.example.snake_bite_api.repository.SnakeRepository;
import com.example.snake_bite_api.repository.SymptomRepository;
import com.example.snake_bite_api.service.SymptomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        return symptom;
    }
}
