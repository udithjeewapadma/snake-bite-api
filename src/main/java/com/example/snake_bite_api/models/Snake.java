package com.example.snake_bite_api.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "snakes")
public class Snake {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String species;
    private String color;
    private String pattern;
    private String region;
    private double averageLength;
    private boolean venomous;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "snake_id")
    private Admin admin;

}
