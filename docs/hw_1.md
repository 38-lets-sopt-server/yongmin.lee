# 에브리타임 자유게시판 게시글 기능 정리

## 1. 기능 개요

### 1-1. 자유게시판 목록
자유게시판에 등록된 게시글 목록을 조회하는 화면.
자유게시판 목록 화면에서는 게시글의 제목, 작성 시간, 작성자 표시(익명 여부), 공감 수, 댓글 수, 조회 수 등의 요약 정보를 확인할 수 있다.
무한 스크롤을 통해 다른 게시글을 계속 탐색할 수 있으며, 특정 게시글을 클릭하여 게시글 상세 조회 화면으로 이동할 수 있다.

### 1-2. 게시글 상세 조회
자유게시판 목록 화면에서 특정 게시글을 클릭하여 이동 가능하다.
상세 화면에서는 게시글 본문, 작성자 정보, 작성 시간, 공감 수, 스크랩 여부, 댓글 목록 등을 확인 할 수 있다.
또한 게시글에 공감하거나 스크랩할 수 있고, 댓글 및 대댓글을 작성할 수 있다.

### 1-3. 글 작성
글 작성 시 제목과 본문을 입력하며, 익명 여부와 질문 여부를 선택할 수 있다.
작성 완료 시 게시글이 저장되고, 자유게시판 목록에 반영된다.

## 2. 화면별 필요 기능 정리

### 2-1. 자유게시판 목록 화면
> 필요한 기능
- 게시글 목록 조회
- 게시글 요약 정보 표시
- 게시글 선택 시 상세 화면 이동
- 무한 스크롤
- 글쓰기 화면 이동
- 인기글 선정

> 화면에 표시할 정보
- 게시글 제목
- 본문 미리보기
- 작성 시간(현재 시간 기준으로 얼마나 지났는지)
- 작성자 표시
- 공감 수
- 댓글 수
- 조회 수
- HOT 게시글 여부
- 게시판 이름
- 학교 이름

### 2-2. 게시글 상세 화면
> 필요한 기능
- 게시글 단건 조회
- 본문 전체 확인
- 공감 기능
- 스크랩 기능
- 댓글 목록 조회
- 대댓글 작성
- 뒤로 가기

> 화면에 표시할 정보
- 게시글 제목
- 작성자 정보
- 작성 시간
- 공감 수, 나의 공감 여부
- 댓글 수
- 스크랩 수, 나의 스크랩 여부
- 각 댓글의 프로필 사진, 작성자 이름, 작성 시간, 내용
- 대댓글 관계 정보

### 2-3. 글쓰기 화면
> 필요한 기능
- 제목 입력
- 본문 입력
- 익명 여부 선택
- 질문글 여부 선택
- 게시글 등록
- 작성 취소 및 뒤로가기
- 첨부파일 추가

> 입력 요소
- 제목
- 본문
- 익명 체크 여부
- 질문글 체크 여부

## 저장해야 하는 데이터

### 3-1. 사용자(User)
- user_id: 회원 id
- email: 회원 이메일
- password: 회원 비밀번호(해싱된 상태로 저장)

### 3-2. 사용자 정보(User_Info)
- user_info_id: 회원 정보 id
- user_id: 회원 id
- nickname: 닉네임
- school_id: 학교 id
- status: 활성/정지 상태 등
- created_at: 계정 생성 시간

### 3-3. 게시글(Post)
- post_id: 게시글 id
- board_id: 게시판 id
- author_id: 작성자 id
- title: 게시글 제목
- content: 본문
- is_anonymous: 익명 여부
- is_question: 질문글 여부
- like_count: 공감 수
- comment_count: 댓글 수
- view_count: 조회 수
- scrap_count: 스크랩 수
- created_at: 작성 일시
- updated_at: 수정 일시


### 3-4. 댓글(Comment)
- comment_id: 댓글 id
- post_id: 게시글 id
- author_id: 작성자 id
- parent_comment_id: 부모 댓글 id
- content: 댓글 내용
- is_anonymous: 익명 여부
- created_at: 작성 일시
- updated_at: 수정 일시

### 3-5. 공감(Like)
- like_id: 공감 id
- post_id: 게시글 식별자
- user_id: 공감한 사용자 id
- created_at: 공감한 시간

### 3-6. 스크랩(Scrap)
- scrap_id: 스크랩 id
- post_id: 게시글 id
- user_id: 스크랩한 사용자 id
- created_at: 스크랩한 시간

## 4. 요청/응답해야 하는 API 정리

### 4-1. 게시글 목록 조회
무한 스크롤 방식으로, 마지막으로 조회한 게시글을 기준으로 다음 데이터를 가져오는 커서 기반 조회 방식 사용

> 요청
GET /boards/{boardId}/posts?cursor=120&size=20

> 요청 시 필요한 값
- boardId: 게시간 ID
- cursor: 현재까지 조회한 마지막 게시글 기준값
- size: 한 번에 조회할 게시글 개수

> 응답 예시
```
{
  "posts": [
    {
      "postId": 150,
      "title": "주 3시간 강의 4번 빠지면 결석 아님?",
      "contentPreview": "맞지?",
      "authorName": "익명",
      "isAnonymous": true,
      "isQuestion": false,
      "likeCount": 3,
      "commentCount": 14,
      "viewCount": 144,
      "createdAt": "2026-04-05T13:20:00"
    },
    {
      "postId": 149,
      "title": "프미나 얼굴만 보면 누가 젤 예쁨?",
      "contentPreview": ".",
      "authorName": "익명",
      "isAnonymous": true,
      "isQuestion": false,
      "likeCount": 3,
      "commentCount": 33,
      "viewCount": 339,
      "createdAt": "2026-04-05T13:10:00"
    }
  ],
  "nextCursor": 149,
  "hasNext": true
}
```

### 4-2. 게시글 상세 조회
> 요청
GET / post/{postId}

> 응답 예시
```
{
  "postId": 101,
  "title": "주 3시간 강의 4번 빠지면 결석 아님?",
  "content": "맞지?",
  "authorName": "익명",
  "isAnonymous": true,
  "isQuestion": false,
  "likeCount": 3,
  "commentCount": 14,
  "viewCount": 145,
  "isLiked": false,
  "isScrapped": false,
  "createdAt": "2026-04-05T13:20:00"
}
```

### 4-3. 게시글 작성
> 요청
POST /boards/{boardId}/posts

> 요청 본문 예시
```
{
  "title": "제목입니다",
  "content": "본문입니다",
  "isAnonymous": true,
  "isQuestion": false
}
```

> 응답 예시
```
{
  "postId": 102,
  "title": "제목입니다",
  "content": "본문입니다",
  "createdAt": "2026-04-05T14:00:00"
}
```

### 4-4-1. 게시글 공감
> 요청
POST /posts/{postId}/likes

> 응답 예시
```
{
  "postId": 101,
  "liked": true,
  "likeCount": 4
}
```

### 4-4-2. 게시글 공감 취소
> 요청
DELETE /posts/{postId}/likes

> 응답 예시
```
{
  "postId": 101,
  "liked": false,
  "likeCount": 3
}
```

### 4-5-1. 게시글 스크랩
> 요청
POST /posts/{postId}/scraps

> 응답 예시
```
{
  "postId": 101,
  "scrapped": true
}
```

### 4-5-2. 게시글 스크랩 취소
> 요청
DELETE /posts/{postId}/scraps

> 응답 예시
```
{
  "postId": 101,
  "scrapped": false
}
```

### 4-6. 댓글 목록 조회
> 요청
GET /posts/{postId}/comments

> 응답 예시
```
{
  "comments": [
    {
      "commentId": 1,
      "authorName": "익명1",
      "content": "그건 주 1회냐 2회냐에 따라 다름",
      "isAnonymous": true,
      "createdAt": "2026-04-05T14:10:00",
      "children": [
        {
          "commentId": 2,
          "authorName": "익명2",
          "content": "아 결석이 아니라 결강이네요",
          "isAnonymous": true,
          "createdAt": "2026-04-05T14:12:00"
        }
      ]
    }
  ]
}
```

### 4-7. 댓글 작성
> 요청
POST /posts/{postId}/comments

> 요청 본문 예시
```
{
  "content": "댓글 내용입니다",
  "isAnonymous": true,
  "parentCommentId": null
}
```

> 응답 예시
```
{
  "commentId": 10,
  "postId": 101,
  "content": "댓글 내용입니다",
  "createdAt": "2026-04-05T14:20:00"
}
```

## 5. 검증해야 하는 조건

### 5-1. 게시글 작성 검증
> 제목 검증
- 제목은 필수 입력이어야 한다.
- 공백만 입력한 제목은 허용하지 않는다.
- 최대 글자 수를 제한해야 한다.

> 본문 검증
- 본문은 필수 입력이어야 한다.
- 공백만 입력한 본문은 허용하지 않는다.
- 최대 글자 수를 제한해야 한다.

> 사용자 검증
- 로그인한 사용자만 글을 작성할 수 있어야 한다.
- 정지된 사용자는 글 작성이 불가능해야 한다.

> 게시판 검증
- 존재하지 않는 게시판에는 게시글을 작성할 수 없어야 한다.

### 5-2. 게시글 조회 검증
- 존재하지 않는 게시글 id일 경우 오류를 반환해야 한다.
- 삭제된 게시글은 조회되지 않거나 "삭제된 게시글"로 처리해야 한다.

### 5-3. 공감 기능 검증
- 한 사용자는 같은 게시글에 중복 공감할 수 없어야 한다.
- 존재하지 않는 게시글에는 공감할 수 없어야 한다.

### 5-4. 스크랩 기능 검증
- 한 사용자는 같은 게시글을 중복 스크랩할 수 없어야 한다.
- 존재하지 않는 게시글은 스크랩할 수 없어야 한다.

### 5-5. 댓글 작성 검증
- 댓글 내용은 필수 입력이어야 한다.
- 공백만 입력한 댓글은 허용하지 않는다.
- 최대 길이를 제한해야 한다.
- 대댓글일 경우 부모 댓글 id가 존재하지 않으면 작성이 불가능해야 한다.
- 부모 댓글이 다른 게시글에 속한 댓글이면 안된다.
