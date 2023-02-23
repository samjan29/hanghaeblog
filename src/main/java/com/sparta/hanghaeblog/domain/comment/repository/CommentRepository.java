package com.sparta.hanghaeblog.domain.comment.repository;

import com.sparta.hanghaeblog.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
