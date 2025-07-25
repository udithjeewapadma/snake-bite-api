package com.example.snake_bite_api.service;

import com.example.snake_bite_api.controller.dto.request.CreateAdminRequestDTO;
import com.example.snake_bite_api.controller.dto.response.AdminInteractionRequestedSnakeResponseDTO;
import com.example.snake_bite_api.controller.dto.response.AdminResponseDTO;
import com.example.snake_bite_api.exception.AdminNotFoundException;
import com.example.snake_bite_api.exception.RequestedNewSnakeNotFoundException;
import com.example.snake_bite_api.models.Admin;

import java.util.List;

public interface AdminService {

    Admin createAdmin(CreateAdminRequestDTO createAdminRequestDTO);

    AdminResponseDTO findAdminById(Long id) throws AdminNotFoundException;

    List<AdminResponseDTO> findAllAdmins();

    void deleteAdminById(Long id) throws AdminNotFoundException;

    Admin updateAdminById(Long id, CreateAdminRequestDTO createAdminRequestDTO) throws AdminNotFoundException;

    AdminInteractionRequestedSnakeResponseDTO approveRequest(Long adminId, Long requestSnakeId)
            throws AdminNotFoundException, RequestedNewSnakeNotFoundException;

    AdminInteractionRequestedSnakeResponseDTO rejectRequest(Long adminId, Long requestSnakeId)
            throws AdminNotFoundException, RequestedNewSnakeNotFoundException;


}