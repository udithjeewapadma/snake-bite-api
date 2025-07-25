package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateSymptomRequestDTO;
import com.example.snake_bite_api.controller.dto.response.SnakeDTO;
import com.example.snake_bite_api.controller.dto.response.SymptomResponseDTO;
import com.example.snake_bite_api.models.Symptom;
import com.example.snake_bite_api.service.SymptomService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/symptoms")
@AllArgsConstructor
public class SymptomController {

    private final SymptomService symptomService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SymptomResponseDTO createSymptom(@RequestParam Long adminId,
                                            @RequestBody CreateSymptomRequestDTO createSymptomRequestDTO) {

        Symptom symptom = symptomService.createSymptom(adminId,createSymptomRequestDTO);
        SymptomResponseDTO symptomResponseDTO = new SymptomResponseDTO();
        symptomResponseDTO.setId(symptom.getId());
        symptomResponseDTO.setName(symptom.getName());
        symptomResponseDTO.setDescription(symptom.getDescription());

        symptomResponseDTO.setSnakeList(symptom.getSnakes().stream()
                .map(snake -> {
                    SnakeDTO snakeDTO = new SnakeDTO();
                    snakeDTO.setId(snake.getId());
                    snakeDTO.setSnakeName(snake.getName());
                    return snakeDTO;
                })
                .toList());
        symptomResponseDTO.setAdminId(symptom.getAdmin().getId());

        return symptomResponseDTO;
    }

    @GetMapping("/{symptom-id}")
    public SymptomResponseDTO findSymptomById(@PathVariable("symptom-id") Long symptomId) {
        return symptomService.findSymptomById(symptomId);
    }

    @GetMapping
    public List<SymptomResponseDTO> findAllSymptoms() {
        return symptomService.findAllSymptoms();
    }

    @DeleteMapping("/{symptom-id}")
    public void deleteSymptomById(@PathVariable("symptom-id") Long symptomId) {
        symptomService.deleteSymptomById(symptomId);
    }

    @PutMapping("/{symptom-id}")
    private SymptomResponseDTO updateSymptomById(@PathVariable("symptom-id") Long id,
                                                 @RequestBody CreateSymptomRequestDTO createSymptomRequestDTO) {

        Symptom symptom = symptomService.updateSymptomById(id,createSymptomRequestDTO);

        SymptomResponseDTO symptomResponseDTO = new SymptomResponseDTO();
        symptomResponseDTO.setId(symptom.getId());
        symptomResponseDTO.setName(symptom.getName());
        symptomResponseDTO.setDescription(symptom.getDescription());

        symptomResponseDTO.setSnakeList(symptom.getSnakes().stream()
                .map(snake -> {
                    SnakeDTO snakeDTO = new SnakeDTO();
                    snakeDTO.setId(snake.getId());
                    snakeDTO.setSnakeName(snake.getName());
                    return snakeDTO;
                })
                .toList());
        symptomResponseDTO.setAdminId(symptom.getAdmin().getId());

        return symptomResponseDTO;
    }

}
