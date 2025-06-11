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

    @ManyToOne
    @JoinTable(name = "user_id")
    private User user;
}
