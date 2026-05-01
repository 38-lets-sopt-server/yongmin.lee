package org.sopt.dto.post.response;

import org.sopt.domain.Post;

import java.time.LocalDateTime;

public record PostListResponse(
        Long id,
        String title,
        String nickname,
        Long likeCount,
        LocalDateTime createdAt
) {
    public static PostListResponse from(Post post, Long likeCount){
        return new PostListResponse(
                post.getId(),
                post.getTitle(),
                post.getUser().getNickname(),
                likeCount,
                post.getCreatedAt()
        );
    }
}
