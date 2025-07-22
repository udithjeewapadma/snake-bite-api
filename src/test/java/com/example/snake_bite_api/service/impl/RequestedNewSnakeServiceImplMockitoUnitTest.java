package com.example.snake_bite_api.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.example.snake_bite_api.controller.dto.request.CreateNewSnakeRequestDTO;
import com.example.snake_bite_api.controller.dto.response.NewSnakeResponseDTO;
import com.example.snake_bite_api.exception.UserNotFoundException;
import com.example.snake_bite_api.models.RequestedNewSnake;
import com.example.snake_bite_api.models.User;
import com.example.snake_bite_api.repository.RequestedNewSnakeRepository;
import com.example.snake_bite_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestedNewSnakeServiceImplMockitoUnitTest {

    @InjectMocks
    private RequestedNewSnakeServiceImpl requestedNewSnakeService;

    @Mock
    private RequestedNewSnakeRepository requestedNewSnakeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRequest_shouldReturnNewSnakeResponseDTO() throws IOException, UserNotFoundException {
        Long userId = 1L;

        CreateNewSnakeRequestDTO dto = new CreateNewSnakeRequestDTO();
        dto.setName("TestSnake");
        dto.setColor("Green");
        dto.setSpecies("SpecTest");
        dto.setPattern("Striped");
        dto.setAverageLength(2.5);
        dto.setImageFiles(Collections.emptyList());

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        RequestedNewSnake savedEntity = new RequestedNewSnake();
        savedEntity.setId(10L);
        savedEntity.setName(dto.getName());
        savedEntity.setColor(dto.getColor());
        savedEntity.setSpecies(dto.getSpecies());
        savedEntity.setPattern(dto.getPattern());
        savedEntity.setAverageLength(dto.getAverageLength());
        savedEntity.setUser(user);
        savedEntity.setVenomous(com.example.snake_bite_api.models.Venomous.NON_VENOMOUS);
        savedEntity.setStatus(com.example.snake_bite_api.models.SnakeRequestStatus.PENDING);
        savedEntity.setImageUrl(Collections.emptyList());

        when(requestedNewSnakeRepository.save(any(RequestedNewSnake.class))).thenReturn(savedEntity);

        NewSnakeResponseDTO responseDTO = requestedNewSnakeService.createRequest(userId, dto);

        assertNotNull(responseDTO);
        assertEquals(savedEntity.getId(), responseDTO.getId());
        assertEquals("TestSnake", responseDTO.getName());
        assertEquals(userId, responseDTO.getUserId());
    }

    @Test
    void createRequest_shouldThrowUserNotFoundException() {
        Long userId = 999L;
        CreateNewSnakeRequestDTO dto = new CreateNewSnakeRequestDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            requestedNewSnakeService.createRequest(userId, dto);
        });
    }

    @Test
    void findAllRequests_shouldReturnListOfDTOs() {
        User user = new User();
        user.setId(5L);

        RequestedNewSnake snake1 = new RequestedNewSnake();
        snake1.setId(1L);
        snake1.setName("Snake1");
        snake1.setUser(user);
        snake1.setColor("Red");
        snake1.setSpecies("Spec1");
        snake1.setPattern("Pattern1");
        snake1.setAverageLength(1.0);
        snake1.setStatus(com.example.snake_bite_api.models.SnakeRequestStatus.PENDING);
        snake1.setVenomous(com.example.snake_bite_api.models.Venomous.NON_VENOMOUS);
        snake1.setImageUrl(Collections.emptyList());

        RequestedNewSnake snake2 = new RequestedNewSnake();
        snake2.setId(2L);
        snake2.setName("Snake2");
        snake2.setUser(user);
        snake2.setColor("Blue");
        snake2.setSpecies("Spec2");
        snake2.setPattern("Pattern2");
        snake2.setAverageLength(2.0);
        snake2.setStatus(com.example.snake_bite_api.models.SnakeRequestStatus.PENDING);
        snake2.setVenomous(com.example.snake_bite_api.models.Venomous.NON_VENOMOUS);
        snake2.setImageUrl(Collections.emptyList());

        when(requestedNewSnakeRepository.findAll()).thenReturn(Arrays.asList(snake1, snake2));

        List<NewSnakeResponseDTO> result = requestedNewSnakeService.findAllRequests();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Snake1", result.get(0).getName());
        assertEquals("Snake2", result.get(1).getName());
    }
}
