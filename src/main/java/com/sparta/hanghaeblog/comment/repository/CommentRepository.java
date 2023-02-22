package com.sparta.hanghaeblog.comment.repository;

import com.sparta.hanghaeblog.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
