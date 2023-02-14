package com.sparta.hanghaeblog.repository;

import com.sparta.hanghaeblog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
