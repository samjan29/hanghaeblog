package com.sparta.hanghaeblog.entity;

import com.sparta.hanghaeblog.dto.PostDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

    public Post(PostDto.Request blogRequestDto, User user) {
        this.title = blogRequestDto.getTitle();
        this.contents = blogRequestDto.getContents();
        this.user = user;
    }

    public void update(PostDto.Request requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }
}
