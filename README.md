# chatting-demo

## 개요 

평소에 관심이 있었고 업무에서 개발해보지 못한 웹에서의 간단한 채팅 기능을 개발해보고 싶어서 시작한 토이 프로젝트

<br>

## 목표

1. 서버에서 페이지를 개발하던 방식에서 벗어나 서버에서는 REST API를 반환하고 프론트 기술을 활용하여 페이지 만들어보자
2. WEB-RTC를 활용하여 과거에 1:1 채팅을 구현해봤으나 그 당시에 구현하지 못했던 다대다 채팅 기능을 구현해보자
3. 새로운 기능 사항을 개발하고 클라우드 인스턴스에 반영하는 과정을 자동화 해보자(진행중)

<br>

## 사용 기술

**Server**
- Java 8
- Spring Boot
- Spring Mvc, Spring Hateoas, Spring Data Jpa, Spring Security
- WebSocket, QueryDsl, Jwt
- Gradle
- Junit5

**Front**
- Html/Css
- Javascript(ES6)
- Vue, Vue Router, Vuetify
- Axios, WEB-RTC

**ETC**
- GCP(GCE)
- Nginx
- Coturn
- MySQL

<br>

## 구성도

<br>

**서버**

![서버 구성도 001](https://user-images.githubusercontent.com/50852963/131971546-8563180c-7f16-4414-861b-f02cece6bed1.png)

**P2P 통신**

![WebRtc-P2P구성도 001](https://user-images.githubusercontent.com/50852963/131971617-69313f50-80bf-46e7-b4bd-192778d10f14.png)

<br>

### 개발 내용 정리 (작성중)

[Dev Wiki](https://www.notion.so/hwanse/823eefcb4a6c42e69ab6c42ad2c88413)
