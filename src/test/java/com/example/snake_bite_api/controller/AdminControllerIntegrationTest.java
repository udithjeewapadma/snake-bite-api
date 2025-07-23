package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateAdminRequestDTO;
import com.example.snake_bite_api.controller.dto.response.AdminResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private CreateAdminRequestDTO sampleAdminRequest;

    @BeforeEach
    void setup() {
        sampleAdminRequest = new CreateAdminRequestDTO();
        sampleAdminRequest.setAdminName("John Doe");
        sampleAdminRequest.setEmail("john@example.com");
        sampleAdminRequest.setPhoneNumber("1234567890");
    }

    @Test
    void testCreateAndGetAdmin() {
        // Create admin
        ResponseEntity<AdminResponseDTO> createResponse = restTemplate.postForEntity(
                "/admins", sampleAdminRequest, AdminResponseDTO.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        AdminResponseDTO createdAdmin = createResponse.getBody();
        assertThat(createdAdmin).isNotNull();
        assertThat(createdAdmin.getId()).isNotNull();
        assertThat(createdAdmin.getAdminName()).isEqualTo(sampleAdminRequest.getAdminName());

        Long adminId = createdAdmin.getId();

        // Get admin by id
        ResponseEntity<AdminResponseDTO> getResponse = restTemplate.getForEntity(
                "/admins/{id}", AdminResponseDTO.class, adminId);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        AdminResponseDTO fetchedAdmin = getResponse.getBody();
        assertThat(fetchedAdmin).isNotNull();
        assertThat(fetchedAdmin.getId()).isEqualTo(adminId);
        assertThat(fetchedAdmin.getEmail()).isEqualTo(sampleAdminRequest.getEmail());
    }

    @Test
    void testGetAllAdmins() {
        // Create one admin for testing
        restTemplate.postForEntity("/admins", sampleAdminRequest, AdminResponseDTO.class);

        ResponseEntity<List> response = restTemplate.getForEntity("/admins", List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void testUpdateAdmin() {
        // Create admin first
        ResponseEntity<AdminResponseDTO> createResponse = restTemplate.postForEntity(
                "/admins", sampleAdminRequest, AdminResponseDTO.class);

        Long adminId = createResponse.getBody().getId();

        CreateAdminRequestDTO updateRequest = new CreateAdminRequestDTO();
        updateRequest.setAdminName("Jane Smith");
        updateRequest.setEmail("jane@example.com");
        updateRequest.setPhoneNumber("0987654321");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateAdminRequestDTO> requestEntity = new HttpEntity<>(updateRequest, headers);

        ResponseEntity<AdminResponseDTO> updateResponse = restTemplate.exchange(
                "/admins/{id}", HttpMethod.PUT, requestEntity, AdminResponseDTO.class, adminId);

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        AdminResponseDTO updatedAdmin = updateResponse.getBody();
        assertThat(updatedAdmin.getAdminName()).isEqualTo("Jane Smith");
        assertThat(updatedAdmin.getEmail()).isEqualTo("jane@example.com");
        assertThat(updatedAdmin.getPhoneNumber()).isEqualTo("0987654321");
    }

    @Test
    void testDeleteAdmin() {
        // Create admin first
        ResponseEntity<AdminResponseDTO> createResponse = restTemplate.postForEntity(
                "/admins", sampleAdminRequest, AdminResponseDTO.class);

        Long adminId = createResponse.getBody().getId();

        restTemplate.delete("/admins/{id}", adminId);

        // Try to get deleted admin - expect 404 or empty
        ResponseEntity<AdminResponseDTO> getResponse = restTemplate.getForEntity(
                "/admins/{id}", AdminResponseDTO.class, adminId);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
