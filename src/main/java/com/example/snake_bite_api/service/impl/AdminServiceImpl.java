package com.example.snake_bite_api.service.impl;

import com.example.snake_bite_api.controller.dto.request.CreateAdminRequestDTO;
import com.example.snake_bite_api.controller.dto.response.AdminResponseDTO;
import com.example.snake_bite_api.exception.AdminNotFoundException;
import com.example.snake_bite_api.models.Admin;
import com.example.snake_bite_api.repository.AdminRepository;
import com.example.snake_bite_api.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Admin createAdmin(CreateAdminRequestDTO createAdminRequestDTO) {
        Admin admin = new Admin();
        admin.setAdminName(createAdminRequestDTO.getAdminName());
        admin.setEmail(createAdminRequestDTO.getEmail());
        admin.setPhoneNumber(createAdminRequestDTO.getPhoneNumber());
        return adminRepository.save(admin);
    }

    @Override
    public AdminResponseDTO findAdminById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found"));
        AdminResponseDTO adminResponseDTO = new AdminResponseDTO();
        adminResponseDTO.setId(admin.getId());
        adminResponseDTO.setAdminName(admin.getAdminName());
        adminResponseDTO.setEmail(admin.getEmail());
        adminResponseDTO.setPhoneNumber(admin.getPhoneNumber());
        return adminResponseDTO;
    }

    @Override
    public List<AdminResponseDTO> findAllAdmins() {
        List<Admin> admins = adminRepository.findAll();
        return admins.stream().map(admin -> {
            AdminResponseDTO adminResponseDTO = new AdminResponseDTO();
            adminResponseDTO.setId(admin.getId());
            adminResponseDTO.setAdminName(admin.getAdminName());
            adminResponseDTO.setEmail(admin.getEmail());
            adminResponseDTO.setPhoneNumber(admin.getPhoneNumber());
            return adminResponseDTO;
        }).collect(Collectors.toList());
    }
}
