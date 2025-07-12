package com.example.snake_bite_api.service;

import com.example.snake_bite_api.controller.dto.request.CreateFirstAidRequestDTO;
import com.example.snake_bite_api.models.FirstAid;

public interface FirstAidService {

    FirstAid createFirstAid(Long adminId, Long symptomId, CreateFirstAidRequestDTO createFirstAidRequestDTO);
}
