package com.example.snake_bite_api.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "blogs")
public class Blog {

    @Id
    private Long id;
    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
