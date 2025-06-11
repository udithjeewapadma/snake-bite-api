package com.example.snake_bite_api.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "replies")
public class Reply {

    @Id
    private Long id;
    private String replyBody;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
