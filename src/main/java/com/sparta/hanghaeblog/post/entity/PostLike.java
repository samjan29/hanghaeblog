package com.sparta.hanghaeblog.post.entity;

import com.sparta.hanghaeblog.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "isShow", nullable = false)
    private Integer isShow;

    public PostLike(Post post, User user) {
        this.post = post;
        this.user = user;
        isShow = 1;
    }

    public void toggle(Integer isShow) {
        this.isShow = isShow;
    }
}
