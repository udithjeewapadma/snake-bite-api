package com.example.snake_bite_api.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "comments")
public class Comment {

    @Id
    private Long id;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
