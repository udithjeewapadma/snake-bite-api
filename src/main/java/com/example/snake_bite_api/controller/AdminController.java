package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateAdminRequestDTO;
import com.example.snake_bite_api.controller.dto.response.AdminInteractionRequestedSnakeResponseDTO;
import com.example.snake_bite_api.controller.dto.response.AdminResponseDTO;
import com.example.snake_bite_api.models.Admin;
import com.example.snake_bite_api.service.AdminService;
import com.example.snake_bite_api.service.RequestedNewSnakeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdminResponseDTO createAdmin(@Valid @RequestBody CreateAdminRequestDTO createAdminRequestDTO) {
        Admin admin = adminService.createAdmin(createAdminRequestDTO);
        AdminResponseDTO adminResponseDTO = new AdminResponseDTO();
        adminResponseDTO.setId(admin.getId());
        adminResponseDTO.setAdminName(admin.getAdminName());
        adminResponseDTO.setEmail(admin.getEmail());
        adminResponseDTO.setPhoneNumber(admin.getPhoneNumber());

        return adminResponseDTO;
    }

    @GetMapping("/{admin-id}")
    public AdminResponseDTO getAdmin(@PathVariable("admin-id") Long adminId) {
        return adminService.findAdminById(adminId);
    }

    @GetMapping
    public List<AdminResponseDTO> getAllAdmins() {
        return adminService.findAllAdmins();
    }

    @DeleteMapping("/{admin-id}")
    public void deleteAdmin(@PathVariable("admin-id") Long adminId) {
        adminService.deleteAdminById(adminId);
    }

    @PutMapping("/{admin-id}")
    public AdminResponseDTO updateAdmin(@PathVariable("admin-id") Long id,
                                         @RequestBody CreateAdminRequestDTO createAdminRequestDTO) {
        Admin admin = adminService.updateAdminById(id, createAdminRequestDTO);
        AdminResponseDTO adminResponseDTO = new AdminResponseDTO();
        adminResponseDTO.setId(admin.getId());
        adminResponseDTO.setAdminName(admin.getAdminName());
        adminResponseDTO.setEmail(admin.getEmail());
        adminResponseDTO.setPhoneNumber(admin.getPhoneNumber());
        return adminResponseDTO;
    }


    @PostMapping("/approve")
    public AdminInteractionRequestedSnakeResponseDTO approveRequest(@RequestParam Long adminId,
                                                                     @RequestParam Long requestSnakeId) {
        return adminService.approveRequest(adminId, requestSnakeId);
    }

    @PostMapping("/reject")
    public AdminInteractionRequestedSnakeResponseDTO rejectRequest(@RequestParam Long adminId,
                                                                    @RequestParam Long requestSnakeId) {
        return adminService.rejectRequest(adminId, requestSnakeId);
    }
}
