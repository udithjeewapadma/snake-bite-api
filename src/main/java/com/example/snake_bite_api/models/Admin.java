package com.example.snake_bite_api.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String adminName;
    private String phoneNumber;
    private String email;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Snake> snakes;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<RequestedNewSnake> requestedNewSnakes;

    @OneToMany
    @ToString.Exclude
    private List<Symptom> symptoms;

    @OneToMany
    @ToString.Exclude
    private List<FirstAid> firstAids;
}
