# Gameinfo Project
게임에 관한 뉴스 및 정보에 관한 커뮤니티 웹 프로젝트

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

## 기능 설명


## 구현 예정 기능

<br>

## API 문서 링크 
https://www.gameinfo.momoon.kro.kr/swagger-ui/index.html
