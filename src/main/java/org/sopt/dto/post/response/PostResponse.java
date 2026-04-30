package org.sopt.dto.post.response;

import org.sopt.domain.Post;
import org.sopt.domain.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// 게시글 조회 응답 (서버 → 클라이언트)
// Post 객체를 그대로 노출하지 않고, 보여줄 것만 골라서 반환
public record PostResponse (
        Long id,
        String title,
        String content,
        Long userId,
        String nickname,
        LocalDateTime createdAt

){

    // Post 객체를 PostResponse로 변환하는 정적 팩토리 메서드
    public static PostResponse from(Post post){
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getNickname(),
                post.getCreatedAt()
        );
    }
}
