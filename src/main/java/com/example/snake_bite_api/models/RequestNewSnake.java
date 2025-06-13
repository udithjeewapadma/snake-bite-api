package com.example.snake_bite_api.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "newSnakes")
public class RequestNewSnake {

    @Id
    private Long id;
    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private SnakeRequestStatus status;

    @ManyToOne
    @JoinTable(name = "user_id")
    private User user;

    @ManyToOne
    @JoinTable(name = "admin_id")
    private Admin admin;
}
