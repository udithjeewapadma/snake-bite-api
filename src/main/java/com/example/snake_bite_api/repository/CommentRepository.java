package com.example.snake_bite_api.repository;

import com.example.snake_bite_api.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
