package com.example.snake_bite_api.repository;

import com.example.snake_bite_api.models.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Long> {
}
