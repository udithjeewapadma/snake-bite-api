package com.example.snake_bite_api.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String adminName;
    private int phoneNumber;
    private String email;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Snake> snakes;

    @OneToMany
    private List<RequestNewSnake> requestNewSnakes;
}
