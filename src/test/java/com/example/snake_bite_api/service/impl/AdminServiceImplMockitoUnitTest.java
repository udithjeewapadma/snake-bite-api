package com.example.snake_bite_api.service.impl;

import com.example.snake_bite_api.controller.dto.request.CreateAdminRequestDTO;
import com.example.snake_bite_api.controller.dto.response.AdminInteractionRequestedSnakeResponseDTO;
import com.example.snake_bite_api.exception.AdminNotFoundException;
import com.example.snake_bite_api.exception.RequestedNewSnakeNotFoundException;
import com.example.snake_bite_api.models.*;
import com.example.snake_bite_api.repository.AdminRepository;
import com.example.snake_bite_api.repository.RequestedNewSnakeRepository;
import com.example.snake_bite_api.repository.SnakeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplMockitoUnitTest {

    @Mock  private AdminRepository adminRepository;
    @Mock  private SnakeRepository snakeRepository;
    @Mock  private RequestedNewSnakeRepository requestedNewSnakeRepository;
    @InjectMocks private AdminServiceImpl adminService;

    private Admin admin;
    private RequestedNewSnake pendingReq;

    @BeforeEach
    void setUp() {

        admin = new Admin();
        admin.setId(1L);
        admin.setAdminName("Alice");
        admin.setEmail("alice@example.com");
        admin.setPhoneNumber("077‑1111111");


        pendingReq = new RequestedNewSnake();
        pendingReq.setId(9L);
        pendingReq.setName("Green Pit Viper");
        pendingReq.setSpecies("Trimeresurus trigonocephalus");
        pendingReq.setColor("Green");
        pendingReq.setPattern("Plain");
        pendingReq.setAverageLength(0.7);
        pendingReq.setVenomous(Venomous.DEADLY);
        pendingReq.setImageUrl(List.of("img-1.jpg"));
        pendingReq.setStatus(SnakeRequestStatus.PENDING);
        pendingReq.setUser(new User());
    }


    @Test
    void createAdmin_saves_and_returns_entity() {
        var dto = new CreateAdminRequestDTO();
        dto.setAdminName("Bob");
        dto.setEmail("bob@example.com");
        dto.setPhoneNumber("071‑2222222");

        when(adminRepository.save(any(Admin.class))).thenAnswer(inv -> {
            Admin toSave = inv.getArgument(0);
            toSave.setId(2L);
            return toSave;
        });

        Admin saved = adminService.createAdmin(dto);

        assertThat(saved.getId()).isEqualTo(2L);
        assertThat(saved.getAdminName()).isEqualTo("Bob");
        verify(adminRepository).save(any(Admin.class));
    }


    @Test
    void findAdminById_returns_dto_when_found() {
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        var result = adminService.findAdminById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getAdminName()).isEqualTo("Alice");
    }

    @Test
    void findAdminById_throws_when_missing() {
        when(adminRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminService.findAdminById(99L))
                .isInstanceOf(AdminNotFoundException.class);
    }


    @Test
    void approveRequest_creates_snake_and_updates_status() {
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(requestedNewSnakeRepository.findById(9L)).thenReturn(Optional.of(pendingReq));
        when(requestedNewSnakeRepository.save(any(RequestedNewSnake.class)))
                .thenAnswer(inv -> inv.getArgument(0));
        when(snakeRepository.save(any(Snake.class))).thenAnswer(inv -> {
            Snake s = inv.getArgument(0);
            s.setId(42L);
            return s;
        });

        AdminInteractionRequestedSnakeResponseDTO dto =
                adminService.approveRequest(1L, 9L);

        assertThat(dto.getStatus()).isEqualTo(SnakeRequestStatus.APPROVED);
        assertThat(dto.getAdminId()).isEqualTo(1L);
        verify(snakeRepository).save(any(Snake.class));
    }

    @Test
    void approveRequest_throws_if_request_missing() {
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(requestedNewSnakeRepository.findById(123L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminService.approveRequest(1L, 123L))
                .isInstanceOf(RequestedNewSnakeNotFoundException.class);
    }


    @Test
    void rejectRequest_sets_status_rejected() {
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(requestedNewSnakeRepository.findById(9L)).thenReturn(Optional.of(pendingReq));
        when(requestedNewSnakeRepository.save(any(RequestedNewSnake.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        var dto = adminService.rejectRequest(1L, 9L);

        assertThat(dto.getStatus()).isEqualTo(SnakeRequestStatus.REJECTED);
    }
}
