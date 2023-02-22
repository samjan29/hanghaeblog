package com.sparta.hanghaeblog.comment.entity;

import com.sparta.hanghaeblog.comment.dto.CommentDto;
import com.sparta.hanghaeblog.common.entity.Timestamped;
import com.sparta.hanghaeblog.post.entity.Post;
import com.sparta.hanghaeblog.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<CommentLike> commentLikes = new ArrayList<>();

    public Comment(CommentDto.Request commentRequestDto, Post post, User user) {
        this.contents = commentRequestDto.getContents();
        this.post = post;
        this.user = user;
    }

    public void update(CommentDto.Request commentRequestDto) {
        this.contents = commentRequestDto.getContents();
    }
}
