package com.example.snake_bite_api.repository;

import com.example.snake_bite_api.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

}
