package org.sopt.dto.post.request;

import org.sopt.domain.User;

// 게시글 작성 요청 (클라이언트 → 서버)
public record CreatePostRequest
        (
          String title,
          String content,
          Long userId
        ){
}
