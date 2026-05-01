package org.sopt.service;

import org.sopt.common.exception.BusinessException;
import org.sopt.common.exception.ErrorCode;
import org.sopt.domain.Like;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.repository.LikeRepository;
import org.sopt.repository.PostRepository;
import org.sopt.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public LikeService(LikeRepository likeRepository, PostRepository postRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 게시글에 좋아요 누르기
    @Transactional
    public void likePost(Long postId, Long userId){
        // user 검증
        User user = findUser(userId);

        // post 검증
        Post post = findPost(postId);

        // like 검증(해당 유저가 해당 게시글에 이미 좋아요를 눌렀는지)
        if(likeRepository.existsByUserIdAndPostId(userId, postId)){
            throw new BusinessException(ErrorCode.ALREADY_LIKED);
        }

        Like like = new Like(user, post);
        likeRepository.save(like);
    }

    // 게시글에 좋아요 취소하기
    @Transactional
    public void unlikePost(Long postId, Long userId){
        // user 검증
        User user = findUser(userId);

        // post 검증
        Post post = findPost(postId);

        if (!likeRepository.existsByUserIdAndPostId(user.getId(), post.getId())){
            throw new BusinessException(ErrorCode.LIKE_NOT_FOUND);
        }

        likeRepository.deleteByUserIdAndPostId(user.getId(), post.getId());
    }

    // 유저 조회 메서드
    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    // 게시글 조회 메서드
    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }


}
