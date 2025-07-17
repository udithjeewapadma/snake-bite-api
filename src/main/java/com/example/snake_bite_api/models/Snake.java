package com.example.snake_bite_api.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

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
    private double averageLength;

    @Enumerated(EnumType.STRING)
    private Venomous venomous;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> imageUrl;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    @ToString.Exclude
    private Admin admin;

    @ManyToMany(mappedBy = "snakes")
    @ToString.Exclude
    private List<Symptom> symptoms;

}
