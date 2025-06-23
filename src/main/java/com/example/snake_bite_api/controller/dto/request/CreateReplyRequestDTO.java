package com.example.snake_bite_api.controller.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class CreateReplyRequestDTO {

    @NotBlank(message = "User name cannot be blank")
    private String replyBody;
}
