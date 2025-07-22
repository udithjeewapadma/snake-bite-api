package com.example.snake_bite_api.service.impl;

import com.example.snake_bite_api.SnakeBiteApiApplication;
import com.example.snake_bite_api.controller.dto.request.CreateNewSnakeRequestDTO;
import com.example.snake_bite_api.controller.dto.response.NewSnakeResponseDTO;
import com.example.snake_bite_api.models.User;
import com.example.snake_bite_api.repository.UserRepository;
import com.example.snake_bite_api.service.RequestedNewSnakeService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SnakeBiteApiApplication.class)
@Transactional
class RequestedNewSnakeServiceImplSpringIntegrationTest {

    @Autowired
    private RequestedNewSnakeService requestedNewSnakeService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserName("integration_test_user");
        // Set other necessary fields on user if required
        testUser = userRepository.save(testUser);
    }

    @Test
    void createRequest_and_findAllRequests_integration() throws IOException {
        CreateNewSnakeRequestDTO dto = new CreateNewSnakeRequestDTO();
        dto.setName("IntegrationSnake");
        dto.setColor("Yellow");
        dto.setSpecies("SpecIntegration");
        dto.setPattern("NoPattern");
        dto.setAverageLength(3.3);
        dto.setImageFiles(Collections.emptyList());

        NewSnakeResponseDTO created = requestedNewSnakeService.createRequest(testUser.getId(), dto);
        assertNotNull(created);
        assertEquals("IntegrationSnake", created.getName());
        assertEquals(testUser.getId(), created.getUserId());

        List<NewSnakeResponseDTO> allRequests = requestedNewSnakeService.findAllRequests();
        assertTrue(allRequests.stream().anyMatch(r -> r.getId().equals(created.getId())));
    }

}
