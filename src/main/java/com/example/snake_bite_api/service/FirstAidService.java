package com.example.snake_bite_api.service;

import com.example.snake_bite_api.controller.dto.request.CreateFirstAidRequestDTO;
import com.example.snake_bite_api.controller.dto.response.FirstAidResponseDTO;
import com.example.snake_bite_api.exception.FirstAidNotFoundException;
import com.example.snake_bite_api.models.FirstAid;

import java.util.List;

public interface FirstAidService {

    FirstAid createFirstAid(Long adminId, Long symptomId, CreateFirstAidRequestDTO createFirstAidRequestDTO);

    FirstAidResponseDTO findFirstAidById(Long id) throws FirstAidNotFoundException;

    List<FirstAidResponseDTO> findAllFirstAids();
}
