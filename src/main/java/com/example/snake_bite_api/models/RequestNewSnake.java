package com.example.snake_bite_api.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "newSnakes")
public class RequestNewSnake {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String species;
    private String color;
    private String pattern;
    private double averageLength;

    @Enumerated(EnumType.STRING)
    private Venomous venomous;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> imageUrl;


    @Enumerated(EnumType.STRING)
    private SnakeRequestStatus status;

    @ManyToOne
    @JoinTable(name = "user_id")
    private User user;

    @ManyToOne
    @JoinTable(name = "admin_id")
    private Admin admin;
}
