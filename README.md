# simple-feed-book
> 사용자들간의 피드 공유 사이트
> 

# 요구사항

- 전체 사용자들에 대한 피드를 볼 수 있다.
    - 피드는 제목, 내용, 작성자명 등으로 이루어져 있다.
- 회원가입 및 로그인을 통해 해당 서비스를 이용할 수 있다.
- 사용자면 누구나 피드를 작성할 수 있다.
- 자기 자신이 작성한 피드들을 모아볼 수 있다.
- 자기 자신이 작성한 피드의 경우 수정 및 삭제가 가능하다.

# 수행 흐름

## 피드 조회 요청

```mermaid
sequenceDiagram
    actor User
    participant Web
    participant Server
    participant DB
    
    User ->> Web : 피드 조회 요청
    Web ->> Server : 피드 페이지 랜더링 요청
    Server ->> DB : 필요 데이터 호출 및 주입
    DB ->> Server : 필요 데이터 전달
    Server ->> Web : 데이터 가공 및 페이지 랜더링
    Web ->> User : 피드 페이지 제공
```

## 피드 작성 요청

```mermaid
sequenceDiagram
    actor User
    participant Web
    participant Server
    participant DB
    
    User ->> Web : 피드 작성 요청
    Web ->> Server : 피드 관련 저장 요소 전달
    Server ->> DB : 신규 피드 저장 혹은 기존 피드 데이터 갱신
```

# server 모듈 의존성

## 실행 환경

- java 17
- spring-boot 3.1.6
    - spring-data-jpa
    - spring-web
    - spring-security
    - spring-configuration-processor
    - spring-devtool
    - spring-docker-compose
- postgres 16
- lombok

## 테스트 환경

- spring-boot 3.1.6
    - spring-boot-test
    - spring-security-test
    - testcontainers