package org.sopt.service;

import org.sopt.common.exception.BusinessException;
import org.sopt.common.exception.ErrorCode;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.post.request.CreatePostRequest;
import org.sopt.dto.post.request.UpdatePostRequest;
import org.sopt.dto.post.response.CreatePostResponse;
import org.sopt.dto.post.response.PostListResponse;
import org.sopt.dto.post.response.PostResponse;
import org.sopt.repository.LikeRepository;
import org.sopt.repository.PostRepository;
import org.sopt.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, LikeRepository likeRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
    }


    // CREATE
    @Transactional
    public CreatePostResponse createPost(CreatePostRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 글쓰기 화면설계서: 제목은 필수, 최대 50자
        if (request.title() == null || request.title().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_TITLE);
        }
        if (request.content() == null || request.content().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_CONTENT);
        }
        Post post = new Post(request.title(), request.content(), user);
        postRepository.save(post);

        return new CreatePostResponse(post.getId(), "✅ 게시글 등록 완료!");
    }

    // READ - 전체
    @Transactional(readOnly = true)
    public List<PostListResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostListResponse> responseList = new ArrayList<>();

        for(Post post: posts){
            Long likeCount = likeRepository.countByPostId(post.getId());
            responseList.add(PostListResponse.from(post, likeCount));
        }

        return responseList;
    }

    // READ - 단건 📝
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        Long likeCount = likeRepository.countByPostId(id);

        return PostResponse.from(post, likeCount);
    }

    // UPDATE
    @Transactional
    public void updatePost(Long id, UpdatePostRequest request) {
        if(request.title() == null || request.title().isBlank()){
            throw new BusinessException(ErrorCode.INVALID_TITLE);
        }

        if(request.content() == null || request.content().isBlank()){
            throw new BusinessException(ErrorCode.INVALID_CONTENT);
        }

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        post.update(request.title(), request.content());

    }

    // DELETE
    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                        .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        postRepository.delete(post);


    }
}
