package com.sparta.hanghaeblog.entity;

import com.sparta.hanghaeblog.dto.BlogDto;
import com.sparta.hanghaeblog.dto.BlogDto.Request;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    private Long userId;

    public Post(BlogDto.Request blogRequestDto, Long userId) {
        this.title = blogRequestDto.getTitle();
        this.contents = blogRequestDto.getContents();
        this.userId = userId;
    }

    public void update(BlogDto.Request requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }
}
