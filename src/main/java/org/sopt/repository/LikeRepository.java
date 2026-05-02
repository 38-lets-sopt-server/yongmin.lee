package org.sopt.repository;

import org.sopt.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    // 같은 유저가 같은 게시글에 이미 좋아요를 눌렀는지 확인
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    // 특정 게시글의 좋아요 개수 조회
    long countByPostId(Long postId);

    // 좋아요 취소
    void deleteByUserIdAndPostId(Long userId, Long postId);

    // 좋아요 삭제
    void deleteByPostId(Long postId);
}
