package com.example.snake_bite_api.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "blogs")
public class Blog {

    @Id
    private Long id;
    private String title;
    private String content;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> imageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;
}
