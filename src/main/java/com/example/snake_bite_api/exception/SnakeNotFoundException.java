package com.example.snake_bite_api.exception;

public class SnakeNotFoundException extends RuntimeException {
    public SnakeNotFoundException(String message) {
        super(message);
    }
}
