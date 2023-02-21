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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")   // name 알아서 정해 줌 Entity_PK
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    @OrderBy(value = "createdAt DESC")
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
