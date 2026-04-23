package org.sopt.dto.post.response;

// 게시글 작성 응답 (서버 → 클라이언트)
public record CreatePostResponse(
        Long id,
        String message
) {
}
