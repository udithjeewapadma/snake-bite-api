package com.example.snake_bite_api.models;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum SnakeRequestStatus {
    PENDING,
    APPROVED,
    REJECTED,
}
