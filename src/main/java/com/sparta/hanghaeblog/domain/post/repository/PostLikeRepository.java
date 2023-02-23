package com.sparta.hanghaeblog.domain.post.repository;

import com.sparta.hanghaeblog.domain.post.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);
    Long countByPostIdAndIsShow(Long postId, Integer isShow);
}
