package org.sopt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.common.response.BaseResponse;
import org.sopt.common.response.SuccessCode;
import org.sopt.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Like", description = "게시글 좋아요 관련 API")
@RestController
@RequestMapping("/api/v1/posts/{postId}/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // 게시글 좋아요
    @Operation(summary = "게시글 좋아요", description = "특정 게시글에 좋아요를 누릅니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 성공"),
            @ApiResponse(responseCode = "400", description = "이미 좋아요를 누른 게시글"),
            @ApiResponse(responseCode = "404", description = "게시글 또는 유저를 찾을 수 없음")
    })

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> likePost(
            @Parameter(description = "좋아요를 누를 게시글 ID", example = "1", required = true)
            @PathVariable("postId") Long postId,
            @Parameter(description = "좋아요를 누르는 유저 ID", example = "1", required = true)
            @RequestParam("userId") Long userId
    ) {
        likeService.likePost(postId, userId);
        return ResponseEntity
                .status(SuccessCode.LIKE_CREATE_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.LIKE_CREATE_SUCCESS, null));
    }

    // 게시글 좋아요 취소
    @Operation(summary = "게시글 좋아요 취소", description = "특정 게시글에 누른 좋아요를 취소합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 취소 성공"),
            @ApiResponse(responseCode = "404", description = "좋아요, 게시글 또는 유저를 찾을 수 없음")
    })

    @DeleteMapping
    public ResponseEntity<BaseResponse<Void>> unlikePost(
            @Parameter(description = "좋아요를 취소할 게시글 ID", example = "1", required = true)
            @PathVariable("postId") Long postId,
            @Parameter(description = "좋아요를 취소하는 유저 ID", example = "1", required = true)
            @RequestParam("userId") Long userId
    ) {
        likeService.unlikePost(postId, userId);
        return ResponseEntity
                .status(SuccessCode.LIKE_DELETE_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.LIKE_DELETE_SUCCESS, null));
    }
}
