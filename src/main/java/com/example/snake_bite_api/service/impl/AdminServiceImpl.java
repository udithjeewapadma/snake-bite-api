package com.example.snake_bite_api.service.impl;

import com.example.snake_bite_api.controller.dto.request.CreateAdminRequestDTO;
import com.example.snake_bite_api.controller.dto.response.AdminInteractionRequestedSnakeResponseDTO;
import com.example.snake_bite_api.controller.dto.response.AdminResponseDTO;
import com.example.snake_bite_api.exception.AdminNotFoundException;
import com.example.snake_bite_api.models.Admin;
import com.example.snake_bite_api.models.Snake;
import com.example.snake_bite_api.models.SnakeRequestStatus;
import com.example.snake_bite_api.repository.AdminRepository;
import com.example.snake_bite_api.repository.RequestedNewSnakeRepository;
import com.example.snake_bite_api.repository.SnakeRepository;
import com.example.snake_bite_api.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private SnakeRepository snakeRepository;

    @Autowired
    private RequestedNewSnakeRepository requestedNewSnakeRepository;

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
                .orElseThrow(() -> new AdminNotFoundException("Admin with ID " + id + " not found."));
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

    @Override
    public void deleteAdminById(Long id) throws AdminNotFoundException {
        if (!adminRepository.existsById(id)) {
            throw new AdminNotFoundException("Admin with ID " + id + " not found.");
        }
        adminRepository.deleteById(id);
    }

    @Override
    public Admin updateAdminById(Long id, CreateAdminRequestDTO createAdminRequestDTO) {

        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException("Admin with ID " + id + " not found."));
        existingAdmin.setAdminName(createAdminRequestDTO.getAdminName());
        existingAdmin.setEmail(createAdminRequestDTO.getEmail());
        existingAdmin.setPhoneNumber(createAdminRequestDTO.getPhoneNumber());
        return adminRepository.save(existingAdmin);
    }

    @Override
    public AdminInteractionRequestedSnakeResponseDTO approveRequest(Long adminId, Long requestSnakeId) {
        var admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        var requestedNewSnake = requestedNewSnakeRepository.findById(requestSnakeId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (requestedNewSnake.getStatus() == SnakeRequestStatus.APPROVED) {
            throw new RuntimeException("This request is already approved");
        }

        // Update request status and admin
        requestedNewSnake.setStatus(SnakeRequestStatus.APPROVED);
        requestedNewSnake.setAdmin(admin);
        requestedNewSnakeRepository.save(requestedNewSnake);

        // Save to snake repo
        Snake snake = new Snake();
        snake.setName(requestedNewSnake.getName());
        snake.setSpecies(requestedNewSnake.getSpecies());
        snake.setColor(requestedNewSnake.getColor());
        snake.setPattern(requestedNewSnake.getPattern());
        snake.setAverageLength(requestedNewSnake.getAverageLength());
        snake.setVenomous(requestedNewSnake.getVenomous());
        snake.setImageUrl(new ArrayList<>(requestedNewSnake.getImageUrl()));
        snake.setAdmin(admin);

        snakeRepository.save(snake);


        AdminInteractionRequestedSnakeResponseDTO dto = new AdminInteractionRequestedSnakeResponseDTO();
        dto.setId(requestedNewSnake.getId());
        dto.setName(requestedNewSnake.getName());
        dto.setColor(requestedNewSnake.getColor());
        dto.setSpecies(requestedNewSnake.getSpecies());
        dto.setPattern(requestedNewSnake.getPattern());
        dto.setAverageLength(requestedNewSnake.getAverageLength());
        dto.setVenomous(requestedNewSnake.getVenomous());
        dto.setImageUrls(requestedNewSnake.getImageUrl());
        dto.setStatus(requestedNewSnake.getStatus());
        dto.setUserId(requestedNewSnake.getUser().getId());
        dto.setAdminId(requestedNewSnake.getAdmin().getId());
        return dto;
    }
}
