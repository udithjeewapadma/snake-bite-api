package com.example.snake_bite_api.repository;

import com.example.snake_bite_api.models.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
