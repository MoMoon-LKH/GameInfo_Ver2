# Gameinfo
> 게임에 관한 정보를 제공하는 웹사이트 서비스 <br>
> 2023.03 ~ 개발중 <br>
> https://www.gameinfo.momoon.kro.kr
<br>

## 개발 환경
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"> <img src="https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white"> <img src="https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white"> <img src="https://img.shields.io/badge/jsonwebtokens-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white"> 

<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"> <img src="https://img.shields.io/badge/yaml-CB171E?style=for-the-badge&logo=yaml&logoColor=white"> <img src="https://img.shields.io/badge/intellijidea-000000?style=for-the-badge&logo=intellijidea&logoColor=white"> 

**ORM**: JPA


**데이터베이스**

<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/amazonrds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white"> <img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white"> 


**배포 환경**

<img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"> <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"> 
 
<br>

## 설정 파일 (yml)
해당 설정 파일은 개인정보가 들어가 있기때문에 gitignore를 통해 커밋에서 제외했습니다
``` yml
spring:
  config:
    activate:
      on-profile: dev ## 개발환경 설정

  jackson:
    time-zone: Asia/Seoul

  ## db설정
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql: ## DB Url
    username: ## DB Id
    password: ## DB Password

  ## jpa 설정
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database: mysql
  servlet:
    multipart:
      max-file-size: 1000MB

  ## redis 설정
  redis:
    host: redis
    port: 6379
    ssl: true

  ## Email 설정
  mail:
    host: smtp.gmail.com
    username: ## 인증 email 주소
    password: ## 인증 email password
    port: 587
    
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher

jwt:
  token-validity-in-second: 300
  refresh-validity-date: 7

image:
  path: ## image 저장할 path
```

<br>

## 로그인 방식
### JWT 토큰 방식
    - 로그인 시 Access Token & Refresh Token 발급
 
#### Access Token
    - 프런트에서 LocalStorage에 저장
    - 토큰이 탈취될 가능성이 있기 때문에 유효시간을 짧게 설정 (5분)
    
#### Refresh Token
    - Cookie로 통해 발급
    - Access Token의 유효시간이 지나게 될 경우
      토큰 재발급 API을 통해 Access Token 재발급 할 때 필요한 토큰 
    - HttpOnly를 통해 js를 통한 수정을 막은 쿠키

<br>

## ERD 설계
<img src="https://github.com/MoMoon-LKH/GameInfo_Ver2/assets/66755342/2de8af63-4219-47f1-885e-035b96d358ae" width="700" />
<br>

## 핵심기능
<details>
 <summary>더보기</summary>
 펼쳐짐
</details>
<br>

## Swagger 
#### API 문서 링크
https://www.gameinfo.momoon.kro.kr/swagger-ui/index.html
<br><br>


## 개선 예정사항
#### 1. news와 post 테이블 통일
    - news와 post를 나누게된 계기는 news가 하드웨어(platform)에 대한 뉴스도 포함하고 있기 때문
    - news = 게임 정보 및 각 플랫폼에 대한 정보, post = 각 게임마다의 게시글로 구성
      그래서 news에서만 platform과의 연관관계가 필요해서 나누게 되었는데
      코드가 더 복잡해지고 동일한 동작 코드가 생기는걸 확인하여서 바꾸기로 하였습니다.
    post 쪽에 platform에 대한 연관관계 설정 및 enum 타입 추가로 구분

#### 2. games와 category의 연관관게 설정 예정
    - 이부분은 아직 설계에서 고민하는 부분입니다
    - 각 게임마다의 다른 카테고리를 만들 수 있게 제공할 지 고정된 카테고리를 제공하게 할지
    -> 각 게임마다의 다른 카테고리를 만들 수 있게 제공 

<br>

