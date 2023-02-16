package com.sparta.hanghaeblog.entity;

import com.sparta.hanghaeblog.dto.CommentDto;
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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(CommentDto.Request commentRequestDto, Post post, User user) {
        this.contents = commentRequestDto.getContents();
        this.post = post;
        this.user = user;
    }

    public void update(CommentDto.Request commentRequestDto) {
        this.contents = commentRequestDto.getContents();
    }
}
