package com.example.snake_bite_api.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "symptoms")
public class Symptom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ManyToMany
    @JoinTable(
            name = "snake_symptoms",
            joinColumns = @JoinColumn(name = "symptom_id"),
            inverseJoinColumns = @JoinColumn(name = "snake_id")
    )
    private List<Snake> snakes;

    @OneToMany(mappedBy = "symptom", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<FirstAid> firstAids;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

}
