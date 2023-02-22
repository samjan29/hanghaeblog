package com.sparta.hanghaeblog.comment.entity;

import com.sparta.hanghaeblog.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "isShow", nullable = false)
    private Integer isShow;

    public CommentLike(Comment comment, User user) {
        this.comment = comment;
        this.user = user;
        isShow = 1;
    }

    public void toggle(Integer isShow) {
        this.isShow = isShow;
    }
}
