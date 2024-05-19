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

## 추가 요구사항

- 좋아요 기능 추가
  - 좋아요를 누를 수 있다.
  - 피드마다 좋아요 개수가 보인다.
- 댓글 기능 추가
  - 피드마다 댓글을 작성할 수 있다.
  - 댓글 작성자와 댓글 내용을 제공한다.
  - 댓글목록은 pagination으로 처리한다.
- 알림 기능 추가
  - 별도 알림 탭에서 신규 알림 목록을 볼 수 있다.
    - 작성 피드의 신규 댓글 알림
    - 좋아요 알림

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

## 좋아요 요청 처리

```mermaid
---
title : 피드별 좋아요 요청
---
sequenceDiagram
Actor User
Participant Web
Participant Server
Participant DB

User ->> Web : 특정 피드 좋아요 클릭
Web ->> Server : 좋아요 처리 위임
Server ->> DB : 해당 피드 및 좋아요 기록 조회
DB ->> Server : 좋아요 정보가 포함된 피드 데이터 반환
Server ->> Server : 요청자에 대한 좋아요 식별
Server -->> DB : 좋아요 기록 삭제 (좋아요 기록이 있는 경우)
Server ->> DB : 좋아요 기록 추가 (좋아요 기록이 없는 경우)
Server ->> DB : 알림 이력 추가 (좋아요)
Server ->> Web : 피드 및 좋아요 데이터 반환
Web ->> User : 피드 및 좋아요 화면 출력
```

## 댓글 목록 조회 요청

```mermaid
---
title :  댓글 목록 조회 요청
---
sequenceDiagram
Actor User
Participant Web
Participant Server
Participant DB

User ->> Web : 특정 피드 내 댓글 목록 조회
Web ->> Server : 피드 식별키 전달
Server ->> DB: 피드 식별키와 연관된 댓글 조회
DB ->> Server : 댓글 데이터 반환
Server ->> Web : 댓글 목록 반환
Web ->> User : 댓글 목록 제공
```

## 댓글 작성 요청

```mermaid
---
title :  댓글 작성 요청
---
sequenceDiagram
Actor User
Participant Web
Participant Server
Participant DB

User ->> Web : 특정 피드 댓글 쓰기
Web ->> Server : 인증 정보 및 댓글 내용 전달
Server ->> DB : 신규 댓글 저장
Server ->> DB : 알림 이력 추가(신규 댓글)
```

## 알림 이력 조회

```mermaid
---
title :  알림 이력 조회 요청
---
sequenceDiagram
Actor User
Participant Web
Participant Server
Participant DB

User ->> Web : 알림 이력 페이지 접근
Web ->> Server : 알림 이력 조회
Server ->> DB : 요청자에 대한 알림 이력 조회 요청
DB ->> Server : 알림 이력 데이터 반환
Server ->> Web : 알림 이력 반환
Web ->> User : 알림 이력 페이지 출력
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
    long id pk "식별키"
    string title "피드 제목"
    string content "피드 내용"
    datetime createAt "생성일자"
    datetime modifiedAt "수정일자"
    int favorites "좋아요 개수"
    long accountId fk "Account 식별키"
}
Account_Favorite_Feed {
    long accountId fk "Account 식별키"
    long feedId fk "Feed 식별키"
}

FEED_COMMENT {
    long id pk "식별키"
    string content "댓글 내용"
    datetime createAt "생성일자"
    datetime modifiedAt "수정일자"
    long feed_id fk "Feed 식별키"
    long account_id fk "Account 식별키"
}

Account ||--o{ Feed : accountId
Account o{--|| Account_Favorite_Feed : accountId
Feed o{--|| Account_Favorite_Feed : feedId

FEED_COMMENT o{--|| Feed: feedId
FEED_COMMENT o{--|| Account: accountId
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