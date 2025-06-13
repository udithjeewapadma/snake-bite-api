package com.example.snake_bite_api.controller.dto.request;

import lombok.Data;

@Data
public class CreateAdminRequestDTO {

    private String adminName;
    private String email;
    private int phoneNumber;
}
