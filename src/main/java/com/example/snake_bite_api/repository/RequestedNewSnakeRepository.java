package com.example.snake_bite_api.repository;

import com.example.snake_bite_api.models.RequestedNewSnake;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestedNewSnakeRepository extends JpaRepository<RequestedNewSnake, Long> {
}
