# HTTP의 멱등성(Idempotency)이란 무엇인가요? GET, POST, PUT, DELETE 중 멱등한 메서드는 무엇인가요?
## HTTP의 멱등성(Idempotency)
HTTP의 멱등성이란 같은 요청을 여러 번 반복해서 보내더라도 서버의 상태가 한 번 요청한 것과 동일하게 유지되는 성질을 의미합니다.
어떤 요청을 1번 보내든 10번 보내든 결과가 동일해야 한다는 것이 핵심입니다.

- 예시
게시글 조회(GET /post/1)을 1번 보내든 10번 보내든 -> 서버 데이터는 변하지 않은(멱등)
게시글 생성 요청(POST /posts)을 여러 번 보내면 -> 게시글이 계속 추가됨(멱등 X)

## HTTP 메서드별 멱등성
### 1. GET(멱등 O)
데이터를 조회하는 요청으로, 서버 상태를 변경하지 않습니다.
여러 번 호출해도 동일한 결과를 리턴하므로, 안전한 메서드입니다.

### 2. POST(멱등 X)
데이터를 생성하는 요청으로, 호출할 때마다 새로운 리소스를 생성할 수 있습니다.
-> 여러 번 호출하면 게시글이 계속 생성됨(멱등 X)

### 3. PUT(멱등 O)
전체 데이터를 덮어쓰기 때문에 같은 요청을 여러 번 보내도 결과는 동일합니다.

### 4. DELETE(멱등 O)
데이터를 삭제합니다.
이미 삭제된 상태에서 다시 삭제하려고 해도 결과는 동일합니다.

# @Controller와 @RestController의 차이는 무엇인가요?
@Controller와 @RestController는 모두 Spring MVC에서 요청을 처리하는 컨트롤러를 정의하는 어노테이션이지만, 응답을 처리하는 방식에서 차이가 있습니다.

## 1. @Controller
@Controller는 전통적인 Spring MVC 방식에서 사용되며, 주로 View를 반환하는 데 사용됩니다.
ViewResolver가 실행되어 템플릿을 찾아 화면을 렌더링하는 방식으로 작동합니다.
JSON을 반환하려면 @ResponseBody 어노테이션을 필요로 합니다.

## 2. RestController
@RestController는 REST API 개발을 위해 만들어진 컨트롤러로, 메서드의 반환값을 자동으로 JSON 형태로 변환하여 응답합니다.
@RestController는 내부적으로 @Controller + @ResponseBody와 같은 의미입니다.
자동으로 Jackson을 사용하여 JSON으로 변환합니다.

# Java Record란 무엇인가요? 기존 클래스와 비교해서 어떤 점이 다른가요?
Java Record는 데이터를 담기 위한 불변 객체를 간결하게 정의하기 위한 Java의 문법입니다.
기존에는 단순히 데이터를 저장하기 위한 클래스를 만들때도 필드, 생성자, getter, equals, hashCode, toString 등을 모두 직접 작성해야 했습니다.
이러한 반복적인 코드를 줄이고, 데이터 중심 객체를 간결하게 표현하기 위해 Record가 도입되었습니다.

## Record의 특징
### 1. 불변 객체(Immutable)
한 번 생성되면 값 변경이 불가능합니다.
-> setter 없음, 모든 필드는 final

### 2. 자동 메서드 생성
Record는 다음을 자동으로 생성합니다.
- 생성자
- equals()
- hashCode()
- toString()

### 3. 데이터 중심 설계
"이 객체는 데이터를 표현하는 용도이다"라는 의도를 명확하게 드러냅니다.

# Optional이란 무엇이고, 왜 null 대신 쓰는 건가요?
Optional은 값이 존재할 수 있고, 존재하지 않을 수 있는 상황을 표현하기 위한 컨테이너 객체입니다.
즉, 어떤 값이 null일 가능성이 있을 떄, 단순히 null을 반환하는 대신 "값이 없을 수도 있음"을 명시적으로 표현하기 위해 사용됩니다.

## null을 사용했을 때 문제점
기존 Java에서는 값이 없을 때 null을 반환했습니다.
항상 null인지 아닌지 체크해야 하고, 만약 체크를 하지 않는다면 NullPointerException이 발생하는 문제가 있었습니다.
그렇기 때문에 개발자에게 "이 값은 값이 있을 수도 있고 없을 수도 있음"을 개발자에게 null가능성을 강제적으로 인식시킵니다.

# Spring Bean의 생명주기란 무엇인가요?
Spring Bean의 생명주기란 Spring 컨테이너가 Bean 객체를 생성하고, 초기화하며, 사용하다가 종료 시점에 소멸시키는 전체 과정을 의미합니다.
즉, Bean이 "생성 -> 초기화 -> 사용 -> 소멸"되는 일련의 흐름을 의미합니다.

## 전체 흐름
객체 생성 -> 의존성 주입 -> 초기화 -> 사용 -> 소멸

### 1. 객체 생성(Instantiation)
Spring 컨테이너가 Bean 클래스를 기반으로 객체를 생성합니다.
Spring이 new 키워드로 객체를 생성합니다.

### 2. 의존성 주입(Dependency Injection)
생성된 객체에 필요한 의존성을 주입합니다.
ex)
```
public PostService(PostRepository postRepository)
```
-> 생성자 주입

### 3. 초기화(Initialization)
Bean이 완전히 사용되기 전에 필요한 초기 작업을 수행하는 단계입니다.
ex) DB 연결, 설정 값 초기화, 캐시 로딩

### 4. 사용 (Ready State)
초기화가 끝난 Bean은 실제 애플리케이션에서 사용됩니다.

### 5. 소멸 (Destruction)
Spring 컨테이너가 종료될 때 Bean을 정리하는 단계입니다.
ex) DB 연결 종료, 리소스 해제

# Spring Boot의 구동 원리를 DispatcherServlet 동작 방식 위주로 정리해주세요
Spring Boot는 내장 서버(Tomcat)을 통해 실행되며,
클라이언트의 모든 HTTP 요청은 DispatcherServlet을 중심으로 처리가됩니다.

DispatcherServlet은 Spring MVC의 핵심으로, 요청을 받아 적절한 컨트롤러로 전달하고, 그 결과를 다시 클라이언트에게 응답하는 역할을 합니다.

### 1. 애플리케이션 실행
```
SpringApplication.run(...)
```
- 내부 동작
내장 Tomcat 실행
Spring 컨테이너 실행
Bean 등록 (Controller, Service 등)
DispatcherServlet 자동 등록

### 2. 요청 발생(HTTP Request)
클라이언트 -> 서버 요청을 가장 먼저 DispatcherServlet이 받음

### 3. DispatcherServlet
DispatcherServlet은 프론트 컨트롤러(Front Contoller)로서, 모든 요청을 중앙에서 처리합니다.

### 4. HanlderMapping(컨트롤러 찾기)
DispatcherServlet은 요청 URL을 기반으로 어떤 컨트롤러가 처리할지 찾습니다.
-> HandlerMapping이 해당 메서드를 매핑

### 5. HandlerAdapter 실행
선택된 컨트롤러를 실제로 실행하기 위해 HandlerAdapter가 호출됩니다.
-> 컨트롤러 메서드를 실행 가능한 형태로 변환

### 6. Controller -> Service -> Repository
컨트롤러에서 비즈니스 로직을 처리합니다.

### 7. Controller 결과 반환
컨트롤러가 결과를 반환합니다.

### 8. DispatcherServlet이 응답 처리
결과를 받아서 클라이언트에게 전달합니다.

- JSON 응답 흐름
```
객체 -> HttpMessageConverter -> JSON 변환 -> 응답
```

- View 응답 흐름
```
ViewResolver -> HTML 렌더링 -> 응답
```