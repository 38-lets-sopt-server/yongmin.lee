package org.sopt.controller;

import org.sopt.common.response.ApiResponse;
import org.sopt.common.response.SuccessCode;
import org.sopt.dto.post.request.CreatePostRequest;
import org.sopt.dto.post.request.UpdatePostRequest;
import org.sopt.dto.post.response.CreatePostResponse;
import org.sopt.dto.post.response.PostResponse;
import org.sopt.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // POST /posts (글 작성)
    @PostMapping
    public ResponseEntity<ApiResponse<CreatePostResponse>> createPost(
            @RequestBody CreatePostRequest request
    ){
        CreatePostResponse response = postService.createPost(request);
        return ResponseEntity
                .status(SuccessCode.POST_CREATE_SUCCESS.getStatus())
                .body(ApiResponse.success(SuccessCode.POST_CREATE_SUCCESS, response));
    }

    // GET /posts (전체 조회)
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPosts(){
        //TODO
        List<PostResponse> response = postService.getAllPosts();
        return ResponseEntity
                .status(SuccessCode.POST_LIST_SUCCESS.getStatus())
                .body(ApiResponse.success(SuccessCode.POST_LIST_SUCCESS, response));
    }

    // GET /posts/{id} (개별 조회)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(
            @PathVariable("id") Long id
    ){
        //TODO
        PostResponse response = postService.getPost(id);
        return ResponseEntity
                .status(SuccessCode.POST_GET_SUCCESS.getStatus())
                .body(ApiResponse.success(SuccessCode.POST_GET_SUCCESS, response));
    }

    // PUT /posts/{id} (글 수정)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updatePost(
            @PathVariable("id") Long id,
            @RequestBody UpdatePostRequest request
            ){
        //TODO
        postService.updatePost(id, request);
        return ResponseEntity
                .status(SuccessCode.POST_UPDATE_SUCCESS.getStatus())
                .body(ApiResponse.success(SuccessCode.POST_UPDATE_SUCCESS, null));
    }

    // DELETE /posts/{id} (글 삭제)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable("id") Long id
    ){
        //TODO
        postService.deletePost(id);
        return ResponseEntity
                .status(SuccessCode.POST_DELETE_SUCCESS.getStatus())
                .body(ApiResponse.success(SuccessCode.POST_DELETE_SUCCESS, null));
    }

}
