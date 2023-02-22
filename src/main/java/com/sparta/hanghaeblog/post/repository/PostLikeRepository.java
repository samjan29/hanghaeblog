package com.sparta.hanghaeblog.post.repository;

import com.sparta.hanghaeblog.post.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);
    Long countByPostIdAndIsShow(Long postId, Integer isShow);
}
