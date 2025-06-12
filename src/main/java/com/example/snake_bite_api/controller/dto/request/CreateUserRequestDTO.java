package com.example.snake_bite_api.controller.dto.request;

import lombok.Data;

@Data
public class CreateUserRequestDTO {

    private String username;
    private String email;
    private String phoneNumber;
}
