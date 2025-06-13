package com.example.snake_bite_api.service.impl;

import com.example.snake_bite_api.controller.dto.request.CreateAdminRequestDTO;
import com.example.snake_bite_api.models.Admin;
import com.example.snake_bite_api.repository.AdminRepository;
import com.example.snake_bite_api.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
