package com.example.snake_bite_api.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

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
    @ToString.Exclude
    @JoinTable(
            name = "snake_symptoms",
            joinColumns = @JoinColumn(name = "symptom_id"),
            inverseJoinColumns = @JoinColumn(name = "snake_id")
    )
    private List<Snake> snakes;

    @OneToMany(mappedBy = "symptom", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<FirstAid> firstAids;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    @ToString.Exclude
    private Admin admin;

}
