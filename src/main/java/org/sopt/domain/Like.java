package org.sopt.domain;

import jakarta.persistence.*;

@Entity
@Table(
        name = "likes",
        // (user_id, post_id) 조합은 테이블에 단 하나만 존재 가능
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "post_id"})
        }
)
public class Like extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 좋아요를 누른 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 좋아요가 눌린 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    protected  Like(){
    }

    public Like(User user, Post post){
        this.user = user;
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Post getPost() {
        return post;
    }

}
