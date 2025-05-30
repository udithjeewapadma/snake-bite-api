package com.example.snake_bite_api.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "firstAids")
public class FirstAid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "symptom_id")
    private Symptom symptom;
}
