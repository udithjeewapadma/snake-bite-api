package com.example.snake_bite_api.service.impl;

import com.example.snake_bite_api.controller.dto.request.CreateAdminRequestDTO;
import com.example.snake_bite_api.models.*;
import com.example.snake_bite_api.repository.AdminRepository;
import com.example.snake_bite_api.repository.RequestedNewSnakeRepository;
import com.example.snake_bite_api.repository.SnakeRepository;
import com.example.snake_bite_api.repository.UserRepository;
import com.example.snake_bite_api.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;



import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class AdminServiceImplSpringIntegrationTest {

    @Autowired private AdminService adminService;

    @Autowired private AdminRepository adminRepository;
    @Autowired private RequestedNewSnakeRepository reqRepo;
    @Autowired private SnakeRepository snakeRepository;
    @Autowired private UserRepository userRepository;

    @Test
    void create_and_fetch_admin_roundtrip() {

        CreateAdminRequestDTO dto = new CreateAdminRequestDTO();
        dto.setAdminName("Bob");
        dto.setEmail("bob@test.com");
        dto.setPhoneNumber("071‑2222222");

        Admin saved = adminService.createAdmin(dto);

        var fetched = adminService.findAdminById(saved.getId());

        assertThat(fetched.getEmail()).isEqualTo("bob@test.com");
    }


    @Test
    void rejectRequest_updates_status() {
        Admin admin = new Admin();
        admin.setAdminName("Eve");
        admin.setEmail("eve@ex.com");
        admin.setPhoneNumber("071-0000000");
        admin = adminRepository.save(admin);

        User reporter = new User();
        reporter.setUserName("reporter1");
        reporter = userRepository.save(reporter);

        RequestedNewSnake req = new RequestedNewSnake();
        req.setName("Copperhead");
        req.setSpecies("Agkistrodon contortrix");
        req.setAverageLength(0.6);
        req.setVenomous(Venomous.DEADLY);
        req.setStatus(SnakeRequestStatus.PENDING);
        req.setUser(reporter);
        req = reqRepo.save(req);

        adminService.rejectRequest(admin.getId(), req.getId());

        RequestedNewSnake updated = reqRepo.findById(req.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(SnakeRequestStatus.REJECTED);
        assertThat(updated.getAdmin().getId()).isEqualTo(admin.getId());
    }

}
