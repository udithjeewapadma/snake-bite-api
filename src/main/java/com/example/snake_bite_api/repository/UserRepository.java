package com.example.snake_bite_api.repository;

import com.example.snake_bite_api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
