package org.sopt.service;

import org.sopt.common.exception.ErrorCode;
import org.sopt.common.exception.customError.InvalidInputException;
import org.sopt.common.exception.customError.PostNotFoundException;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.post.request.CreatePostRequest;
import org.sopt.dto.post.request.UpdatePostRequest;
import org.sopt.dto.post.response.CreatePostResponse;
import org.sopt.dto.post.response.PostResponse;
import org.sopt.repository.PostRepository;
import org.sopt.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    // CREATE
    @Transactional
    public CreatePostResponse createPost(CreatePostRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // 글쓰기 화면설계서: 제목은 필수, 최대 50자
        if (request.title() == null || request.title().isBlank()) {
            throw new InvalidInputException(ErrorCode.INVALID_TITLE);
        }
        if (request.content() == null || request.content().isBlank()) {
            throw new InvalidInputException(ErrorCode.INVALID_CONTENT);
        }
        LocalDateTime createdAt = LocalDateTime.now();
        Post post = new Post(request.title(), request.content(), user, createdAt);
        postRepository.save(post);

        return new CreatePostResponse(post.getId(), "✅ 게시글 등록 완료!");
    }

    // READ - 전체
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostResponse> responseList = new ArrayList<>();

        for(Post post: posts){
            responseList.add(PostResponse.from(post));
        }

        return responseList;
    }

    // READ - 단건 📝
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        return PostResponse.from(post);
    }

    // UPDATE
    @Transactional
    public void updatePost(Long id, UpdatePostRequest request) {
        if(request.title() == null || request.title().isBlank()){
            throw new InvalidInputException(ErrorCode.INVALID_TITLE);
        }

        if(request.content() == null || request.content().isBlank()){
            throw new InvalidInputException(ErrorCode.INVALID_CONTENT);
        }

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        post.update(request.title(), request.content());

    }

    // DELETE
    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);


    }
}
