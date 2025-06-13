package com.example.snake_bite_api.controller.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminResponseDTO {

    private Long id;
    private String adminName;
    private String email;
    private String phoneNumber;
}
