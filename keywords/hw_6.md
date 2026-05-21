# OAuth 2.0이란 무엇인가요? 인증 흐름을 정리해보세요.
## OAuth 2.0
OAuth 2.0은 사용자가 자신의 아이디와 비밀번호를 직접 애플리케이션에 제공하지 않고,
외부 인증 서버를 통해 특정 서비스에 접근 권한을 위임할 수 있도록 하는 인증/인가 표준입니다.

즉, 사용자는 카카오나 구글 같은 외부 서비스에 로그인하고,
우리 서비스는 외부 인증 서버로부터 사용자 정보를 받아 자체 로그인 처리를 할 수 있습니다.

- 예시
사용자가 우리 서비스에 카카오 로그인을 요청
-> 카카오 로그인 화면으로 이동
-> 사용자가 카카오 계정으로 로그인 및 동의
-> 우리 서버가 카카오에서 사용자 정보를 받아옴
-> 우리 서버가 자체 JWT를 발급

## OAuth 2.0의 주요 구성 요소
### 1. Resource Owner
Resource Owner는 자신의 정보를 소유한 사용자입니다.
예를 들어 카카오 계정을 가진 사용자가 Resource Owner입니다.

### 2. Client
Client는 사용자의 정보를 사용하려는 애플리케이션입니다.
우리 에브리타임 클론 서버 또는 프론트엔드가 Client 역할을 합니다.

### 3. Authorization Server
Authorization Server는 사용자를 인증하고, 인가 코드를 발급하는 서버입니다.
카카오 로그인에서는 카카오 인증 서버가 이 역할을 합니다.

### 4. Resource Server
Resource Server는 실제 사용자 정보를 가지고 있는 서버입니다.
카카오 사용자 정보 API 서버가 Resource Server입니다.

## OAuth 2.0 인증 흐름
### 1. 사용자가 소셜 로그인 요청
사용자가 우리 서비스에서 카카오 로그인 버튼을 클릭합니다.
그러면 우리 서버 또는 프론트엔드는 카카오 인가 URL로 사용자를 이동시킵니다.

```
GET https://kauth.kakao.com/oauth/authorize
```

### 2. 외부 인증 서버 로그인 및 동의
사용자는 카카오 로그인 화면에서 로그인하고,
우리 서비스가 이메일, 닉네임 같은 정보를 가져가는 것에 동의합니다.

### 3. Authorization Code 발급
로그인과 동의가 끝나면 카카오는 우리 서비스의 Redirect URI로 사용자를 돌려보냅니다.
이때 URL에 Authorization Code를 함께 전달합니다.

```
http://localhost:8080/api/v1/oauth2/kakao/callback?code=인가코드
```

Authorization Code는 Access Token을 발급받기 위한 일회용 코드입니다.
한 번 사용하면 다시 사용할 수 없습니다.

### 4. 우리 서버가 카카오 Access Token 요청
우리 서버는 전달받은 Authorization Code를 카카오 토큰 API에 보내서
카카오 Access Token을 발급받습니다.

```
POST https://kauth.kakao.com/oauth/token
```

이때 필요한 값은 다음과 같습니다.
- grant_type
- client_id
- redirect_uri
- code

### 5. 카카오 사용자 정보 요청
우리 서버는 카카오 Access Token을 사용해서 카카오 사용자 정보 API를 호출합니다.

```
GET https://kapi.kakao.com/v2/user/me
```

이 요청을 통해 카카오 사용자 고유 ID, 이메일, 닉네임 등의 정보를 받을 수 있습니다.

### 6. 우리 서비스 유저 조회 또는 회원가입
카카오에서 받은 사용자 고유 ID를 기준으로 우리 DB에서 유저를 찾습니다.

- 기존 유저가 있으면 로그인 처리
- 기존 유저가 없으면 자동 회원가입 처리

예를 들어 카카오 로그인 유저는 다음과 같이 저장할 수 있습니다.

```
provider = KAKAO
providerId = 카카오 사용자 고유 ID
email = 카카오 이메일
nickname = 카카오 닉네임
```

### 7. 우리 서버 JWT 발급
카카오 로그인에 성공했다고 해서 카카오 Access Token을 우리 서비스 인증에 그대로 사용하지 않습니다.
우리 서버는 카카오 사용자 정보를 기반으로 자체 JWT를 발급합니다.

```
Access Token + Refresh Token 발급
```

이후 클라이언트는 우리 서버 API를 호출할 때 Authorization 헤더에 우리 서버 Access Token을 담아 요청합니다.

```
Authorization: Bearer 우리서버AccessToken
```

## 전체 흐름 정리
```
사용자 -> 우리 서비스: 카카오 로그인 요청
우리 서비스 -> 카카오 인증 서버: 인가 URL로 이동
사용자 -> 카카오 인증 서버: 로그인 및 동의
카카오 인증 서버 -> 우리 서비스: Authorization Code 전달
우리 서버 -> 카카오 인증 서버: Code로 카카오 Access Token 요청
우리 서버 -> 카카오 Resource Server: 카카오 사용자 정보 요청
우리 서버 -> DB: 유저 조회 또는 자동 회원가입
우리 서버 -> 사용자: 우리 서버 JWT 발급
```

## OAuth 2.0을 사용하는 이유
- 사용자의 비밀번호를 우리 서비스가 직접 저장하지 않아도 됨
- 카카오, 구글 같은 신뢰된 외부 인증 서버를 사용할 수 있음
- 사용자는 별도 회원가입 없이 간편하게 로그인할 수 있음
- 우리 서버는 외부 사용자 정보를 기반으로 자체 JWT를 발급하여 기존 인증 구조와 연결할 수 있음

## Access Token과 Authorization Code의 차이
### Authorization Code
Authorization Code는 Access Token을 발급받기 위한 일회용 코드입니다.
사용자가 외부 인증 서버에서 로그인과 동의를 완료하면 Redirect URI로 전달됩니다.

### Access Token
Access Token은 특정 리소스에 접근하기 위한 토큰입니다.
카카오 Access Token은 카카오 사용자 정보 API를 호출할 때 사용하고,
우리 서버 Access Token은 우리 서비스 API를 호출할 때 사용합니다.

즉, 카카오 Access Token과 우리 서버 JWT Access Token은 서로 다른 토큰입니다.
