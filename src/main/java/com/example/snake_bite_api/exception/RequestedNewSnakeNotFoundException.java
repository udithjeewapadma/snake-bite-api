package com.example.snake_bite_api.exception;

public class RequestedNewSnakeNotFoundException extends RuntimeException {
    public RequestedNewSnakeNotFoundException(String message) {
        super(message);
    }
}
