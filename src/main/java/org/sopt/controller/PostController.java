package org.sopt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.common.response.BaseResponse;
import org.sopt.common.response.SuccessCode;
import org.sopt.dto.post.request.CreatePostRequest;
import org.sopt.dto.post.request.UpdatePostRequest;
import org.sopt.dto.post.response.CreatePostResponse;
import org.sopt.dto.post.response.PostListResponse;
import org.sopt.dto.post.response.PostResponse;
import org.sopt.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Post", description = "게시글 관련 API")
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // POST /posts (글 작성)
    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "게시글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패"),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
    })
    @PostMapping
    public ResponseEntity<BaseResponse<CreatePostResponse>> createPost(
            @RequestBody CreatePostRequest request
    ){
        CreatePostResponse response = postService.createPost(request);
        return ResponseEntity
                .status(SuccessCode.POST_CREATE_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.POST_CREATE_SUCCESS, response));
    }

    // GET /posts (전체 조회)
    @Operation(
            summary = "게시글 전체 조회",
            description = "모든 게시글 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping
    public ResponseEntity<BaseResponse<List<PostListResponse>>> getAllPosts(){
        List<PostListResponse> response = postService.getAllPosts();
        return ResponseEntity
                .status(SuccessCode.POST_LIST_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.POST_LIST_SUCCESS, response));
    }

    // GET /posts/{id} (개별 조회)
    @Operation(
            summary = "게시글 단건 조회",
            description = "게시글 ID를 통해 특정 게시글을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ID 형식 오류)")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<PostResponse>> getPost(
            @PathVariable("id") Long id
    ){
        //TODO
        PostResponse response = postService.getPost(id);
        return ResponseEntity
                .status(SuccessCode.POST_GET_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.POST_GET_SUCCESS, response));
    }

    // PUT /posts/{id} (글 수정)
    @Operation(
            summary = "게시글 수정",
            description = "게시글 ID를 통해 특정 게시글의 제목과 내용을 수정합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패 또는 잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> updatePost(
            @Parameter(description = "수정할 게시글 ID", example = "1", required = true)
            @PathVariable("id") Long id,
            @RequestBody UpdatePostRequest request
            ){
        postService.updatePost(id, request);
        return ResponseEntity
                .status(SuccessCode.POST_UPDATE_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.POST_UPDATE_SUCCESS, null));
    }

    // DELETE /posts/{id} (글 삭제)
    @Operation(
            summary = "게시글 삭제",
            description = "게시글 ID를 통해 특정 게시글을 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ID 형식 오류)")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deletePost(
            @Parameter(description = "삭제할 게시글 ID", example = "1", required = true)
            @PathVariable("id") Long id
    ){
        postService.deletePost(id);
        return ResponseEntity
                .status(SuccessCode.POST_DELETE_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.POST_DELETE_SUCCESS, null));
    }

}
