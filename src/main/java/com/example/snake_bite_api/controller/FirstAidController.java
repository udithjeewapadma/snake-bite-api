package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateFirstAidRequestDTO;
import com.example.snake_bite_api.controller.dto.response.FirstAidResponseDTO;
import com.example.snake_bite_api.models.FirstAid;
import com.example.snake_bite_api.service.FirstAidService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aids")
@AllArgsConstructor
public class FirstAidController {

    private final FirstAidService firstAidService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private FirstAidResponseDTO createFirstAid(@RequestParam Long adminId,
                                               @RequestParam Long symptomId,
                                               @RequestBody CreateFirstAidRequestDTO createFirstAidRequestDTO) {
        FirstAid firstAid = firstAidService.createFirstAid(adminId,symptomId,createFirstAidRequestDTO);

        FirstAidResponseDTO firstAidResponseDTO = new FirstAidResponseDTO();

        firstAidResponseDTO.setId(firstAid.getId());
        firstAidResponseDTO.setName(firstAid.getName());
        firstAidResponseDTO.setDescription(firstAid.getDescription());
        firstAidResponseDTO.setSymptomId(firstAid.getSymptom().getId());
        firstAidResponseDTO.setAdminId(firstAid.getAdmin().getId());

        return firstAidResponseDTO;
    }

    @GetMapping("/{aid-id}")
    private FirstAidResponseDTO findFirstAidById(@PathVariable("aid-id") Long aidId) {
        return firstAidService.findFirstAidById(aidId);
    }

    @GetMapping
    private List<FirstAidResponseDTO> findAllFirstAids() {
        return firstAidService.findAllFirstAids();
    }

    @DeleteMapping("/{aid-id}")
    private void deleteFirstAidById(@PathVariable("aid-id") Long aidId) {
        firstAidService.deleteFirstAidById(aidId);
    }
}
