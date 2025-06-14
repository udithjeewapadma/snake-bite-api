package com.example.snake_bite_api.repository;

import com.example.snake_bite_api.models.Snake;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnakeRepository extends JpaRepository<Snake, Long> {
}
