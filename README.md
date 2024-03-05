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

## 회원가입 요청

```mermaid
sequenceDiagram
actor User
participant Web
participant Server
participant DB

User ->> Web : 회원가입 요청
Web ->> Server : 회원 가입 정보 전달
Server ->> DB : 회원 정보 조회
DB ->> Server : 기가입 회원 여부 반환
Server ->> DB : 신규 회원 정보 저장
Server -->> Web : 이미 가입된 회원 Exception 전달
Server ->> Web : 신규회원 등록 완료
Web ->> User : 가입 요청 결과 전달
```

## 로그인 요청

```mermaid
sequenceDiagram
actor User
participant Web
participant Server
participant DB

User ->> Web : 로그인 요청
Web ->> Server : 로그인 정보가 없을 시 로그인 요청
Server ->> DB : 해당 회원 정보 조회

DB -->> Server : 해당 회원을 찾지 못할 경우
Server -->> Web : Unauthorization Exception
Web -->> User : 로그인 오류 전달

DB ->> Server : 조회된 회원 정보 전달
Server ->> Server : JWT 토큰 생성
Server ->> Web : 발급 토큰 반환
Web ->> User : 토큰 저장
```

## 인증/인가 처리

```mermaid
sequenceDiagram
actor User
participant Web
participant Server
participant DB

User ->> Web : 인증/인가 필요 요청
Web ->> Server : 헤더 내 JWT 토큰 조회
Server ->> Server : 토큰 복호화
Server -->> Web : 복호화 실패 시 에러 전달
Web -->> User : 인증/인가 오류 전달
Server ->> DB : 복호화된 정보 기반으로 회원 조회
DB -->> Server : 조회 실패 시
Server -->> Web : 조회 실패 오류 전달
Web -->> User : 인증/인가 오류 전달
DB ->> Server : 조회된 회원 정보 반환
Server ->> Server : 인증/인가 성공
Server ->> Web : 응답 데이터 반환
Web ->> User : 응답 데이터 가공 및 전달
```

# 도메인 구조

## ERD

```mermaid
---
title : simple-feed-book ERD
---
erDiagram
Account {
    long id pk "식별키"
    string username uk "사용자 아이디"
    string password "암호화 된 비밀번호"
    string nickname "사용자 이름"
    datetime createAt "생성일자"
    datetime modifedAt "수정일자"
}
Feed {
    long id fk "식별키"
    string title "피드 제목"
    string content "피드 내용"
    datetime createAt "생성일자"
    datetime modifiedAt "수정일자"
    long accountId fk "Account 식별키"
}

Account ||--o{ Feed : accountId
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