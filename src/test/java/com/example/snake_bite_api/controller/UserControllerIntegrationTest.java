package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateUserRequestDTO;
import com.example.snake_bite_api.controller.dto.response.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final String baseUrl = "/users";

    @Test
    void testCreateGetUpdateDeleteUser() {
        // 1. Create user
        CreateUserRequestDTO createUser = new CreateUserRequestDTO();
        createUser.setUserName("testuser");
        createUser.setEmail("testuser@example.com");
        createUser.setPhoneNumber("1234567890");

        ResponseEntity<UserResponseDTO> createResponse = restTemplate.postForEntity(
                baseUrl, createUser, UserResponseDTO.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UserResponseDTO createdUser = createResponse.getBody();
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isNotNull();
        assertThat(createdUser.getUserName()).isEqualTo("testuser");

        Long userId = createdUser.getId();

        // 2. Get user by ID
        ResponseEntity<UserResponseDTO> getResponse = restTemplate.getForEntity(
                baseUrl + "/" + userId, UserResponseDTO.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserResponseDTO fetchedUser = getResponse.getBody();
        assertThat(fetchedUser).isNotNull();
        assertThat(fetchedUser.getId()).isEqualTo(userId);
        assertThat(fetchedUser.getUserName()).isEqualTo("testuser");

        // 3. Update user
        CreateUserRequestDTO updateUser = new CreateUserRequestDTO();
        updateUser.setUserName("updateduser");
        updateUser.setEmail("updateduser@example.com");
        updateUser.setPhoneNumber("0987654321");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateUserRequestDTO> updateRequest = new HttpEntity<>(updateUser, headers);

        ResponseEntity<UserResponseDTO> updateResponse = restTemplate.exchange(
                baseUrl + "/" + userId,
                HttpMethod.PUT,
                updateRequest,
                UserResponseDTO.class);

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserResponseDTO updatedUser = updateResponse.getBody();
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getUserName()).isEqualTo("updateduser");

        // 4. Get all users
        ResponseEntity<UserResponseDTO[]> allUsersResponse = restTemplate.getForEntity(
                baseUrl, UserResponseDTO[].class);
        assertThat(allUsersResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserResponseDTO[] users = allUsersResponse.getBody();
        assertThat(users).isNotEmpty();

        // 5. Delete user
        restTemplate.delete(baseUrl + "/" + userId);

        // 6. Confirm deletion
        ResponseEntity<String> deletedGetResponse = restTemplate.getForEntity(
                baseUrl + "/" + userId, String.class);
        assertThat(deletedGetResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
