package com.sparta.hanghaeblog.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(String contents, Post post) {
        this.contents = contents;
        this.post = post;
    }

    public void update(String contents) {
        this.contents = contents;
    }
}
