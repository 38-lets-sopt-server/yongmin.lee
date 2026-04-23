package org.sopt.service;

import org.sopt.common.exception.ErrorCode;
import org.sopt.common.exception.customError.InvalidInputException;
import org.sopt.common.exception.customError.PostNotFoundException;
import org.sopt.domain.Post;
import org.sopt.dto.post.request.CreatePostRequest;
import org.sopt.dto.post.request.UpdatePostRequest;
import org.sopt.dto.post.response.CreatePostResponse;
import org.sopt.dto.post.response.PostResponse;
import org.sopt.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }


    // CREATE ✅ 같이 구현
    // 글쓰기 화면에서 "완료" 버튼을 누르면 이 메서드가 호출돼요
    public CreatePostResponse createPost(CreatePostRequest request) {


        // 글쓰기 화면설계서: 제목은 필수, 최대 50자
        if (request.title() == null || request.title().isBlank()) {
            throw new InvalidInputException(ErrorCode.INVALID_TITLE);
        }
        if (request.content() == null || request.content().isBlank()) {
            throw new InvalidInputException(ErrorCode.INVALID_CONTENT);
        }
        LocalDateTime createdAt = LocalDateTime.now();
        Post post = new Post(postRepository.generateId(), request.title(), request.content(), request.author(), createdAt);
        postRepository.save(post);
        return new CreatePostResponse(post.getId(), "✅ 게시글 등록 완료!");
    }

    // READ - 전체 📝 과제
    // 자유게시판 목록 화면에서 호출돼요
    public List<PostResponse> getAllPosts() {
        // TODO: postList가 비어있으면 "등록된 게시글이 없습니다." 출력 <- Main에서 함
        // TODO: 비어있지 않으면 모든 게시글의 getInfo()를 순서대로 출력
        List<Post> posts = postRepository.findAll();
        List<PostResponse> responseList = new ArrayList<>();

        for(Post post: posts){
            responseList.add(PostResponse.from(post));
        }

        return responseList;
    }

    // READ - 단건 📝 과제
    // 목록에서 특정 게시글을 탭하면 호출돼요 (게시글 상세 화면)
    public PostResponse getPost(Long id) {
        // TODO: postList에서 id가 일치하는 게시글을 찾아 getInfo() 출력
        // TODO: 없으면 "해당 게시글을 찾을 수 없습니다." 출력
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        return PostResponse.from(post);
    }

    // UPDATE 📝 과제
    // 게시글 수정 화면에서 "완료"를 누르면 호출돼요
    public void updatePost(Long id, UpdatePostRequest request) {
        // TODO: postList에서 id가 일치하는 게시글을 찾아 update() 호출
        // TODO: 없으면 "해당 게시글을 찾을 수 없습니다." 출력
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

    // DELETE 📝 과제
    // 게시글 상세에서 삭제를 누르면 호출돼요
    public void deletePost(Long id) {
        // TODO: postList에서 id가 일치하는 게시글을 제거
        // TODO: 성공하면 "삭제 완료!", 없으면 "해당 게시글을 찾을 수 없습니다." 출력
        boolean deleted = postRepository.deleteById(id);

        if(!deleted){
            throw new PostNotFoundException(id);
        }
    }
}
