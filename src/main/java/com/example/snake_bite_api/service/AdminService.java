package com.example.snake_bite_api.service;

import com.example.snake_bite_api.controller.dto.request.CreateAdminRequestDTO;
import com.example.snake_bite_api.controller.dto.response.AdminResponseDTO;
import com.example.snake_bite_api.models.Admin;

import java.util.List;

public interface AdminService {

    Admin createAdmin(CreateAdminRequestDTO createAdminRequestDTO);

    AdminResponseDTO findAdminById(Long id);

    List<AdminResponseDTO> findAllAdmins();

}