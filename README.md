# 차량 번호판 인식 AI 모델 개발, 차량 출입 관리 웹 서비스 개발하기

## 1. 프로젝트 소개 
- 개요: AI 모델을 통해 출입하는 차량의 번호판을 판별하고, 그 기록을 관리하는 웹서비스
- 활용 데이터 : 현장 CCTV 사진, 번호판 사진
- 기간: 2023.08.21 ~ 2023.09.22

## 2. 팀원 
|<img width="200" alt="image" src="https://avatars.githubusercontent.com/u/129818813?v=4">|<img width="200" alt="image" src="https://avatars.githubusercontent.com/u/98063854?v=4">|<img width="200" alt="image" src="https://avatars.githubusercontent.com/u/70638717?v=4">|<img width="200" alt="image" src="https://avatars.githubusercontent.com/u/86204430?v=4">|
| :---------------------------------: | :-----------------------------------:| :---------------------------------: | :-----------------------------------:|
|                Front-End           |           Back-End                       |              AI 모델 개발         |           AI 모델 개발                |       
|             김은진            |          김재민            |                          김민범                  |          최호진                      |      
|[GitHub](https://github.com/EUNJIN6131)|[GitHub](https://github.com/JaeMin1130)|[GitHub](https://github.com/sou05091/)|[GitHub](https://github.com/Gansaw/)|

## 3. 활용 스택 
<h3>Environment</h3>
<div>
  <img src="https://img.shields.io/badge/vscode 1.18.1-007ACC?style=for-the-badge&logo=visualstudiocode&logoColor=white">
</div>
<h3>Config</h3>
<div>
   <img src="https://img.shields.io/badge/npm 9.5.1-CB3837?style=for-the-badge&logo=npm&logoColor=white">
   <img src="https://img.shields.io/badge/maven 3.9.3-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white">
</div>
  <h3>Development</h3>
<div>
    <img src="https://img.shields.io/badge/node.js 18.16.0-339933?style=for-the-badge&logo=Node.js&logoColor=white">
  <img src="https://img.shields.io/badge/react 18.2.0-61DAFB?style=for-the-badge&logo=react&logoColor=white"> 
  <img src="https://img.shields.io/badge/mui 5.14.1-007FFF?style=for-the-badge&logo=mui&logoColor=white" />
</div>
<div>
  <img src="https://img.shields.io/badge/java 17-007396?style=for-the-badge&logo=java&logoColor=white"> 
  <img src="https://img.shields.io/badge/spring boot 3.1.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> 
  <img src="https://img.shields.io/badge/spring security 6.1.1-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"> 
  <img src="https://img.shields.io/badge/spring data 3.1.4-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> 
  <img src="https://img.shields.io/badge/mysql 8.0.32-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
</div>
<div>
  <img src="https://img.shields.io/badge/python 3.10.9-3776AB?style=for-the-badge&logo=python&logoColor=white"> 
  <img src="https://img.shields.io/badge/anaconda3 -44A833?style=for-the-badge&logo=anaconda&logoColor=white"> 
  <img src="https://img.shields.io/badge/flask 2.2.2-000000?style=for-the-badge&logo=flask&logoColor=white"> 
  <img src="https://img.shields.io/badge/yolo v5 -00FFFF?style=for-the-badge&logo=yolo&logoColor=white"> 
  <img src="https://img.shields.io/badge/tensor flow 2.13.0-FF6F00?style=for-the-badge&logo=TensorFlow&logoColor=white"> 
</div>
<h3>Communication</h3>
<div>
  <a href="https://shrub-snap-550.notion.site/6e3827cac0a846c393106e0dfec6ac6e?v=c805bf85a004454695cc77a7968262b5&pvs=4"><img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white"> </a>
    <a href="https://github.com/EUNJIN6131/MiniProject_LicensePlate"><img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"></a>
</div>

## 4. 주요 기능 
- [x] 회원가입/로그인
- [x] 출입 차량 실시간 기록 기능  
- [x] 날짜별 출입 기록 조회 기능
- [x] 차량 번호별 출입 기록 조회 기능
- [x] 기록 수정 기능
- [x] 기록 삭제 기능
- [x] 출입 기록 수정/삭제 히스토리 기능 

## 5. 서비스 아키텍처
![image](https://github.com/JaeMin1130/MiniProject_LicensePlate/assets/98063854/5ccd2dbc-3513-49d8-9ca4-911bd96507e3)


## 6. REST API 명세 
### Spring Boot(port:8080)
| ID | Domain | Method | URI | Description |
| --- | --- | --- | --- | --- |
| 1 | member | POST  | api/members/sign-up | 회원가입 |
| 2 | member | POST | api/members/sign-in | 로그인 |
| 3 | record | POST | api/records | 차량 출입 기록 |
| 4 | record | GET | api/records/date/{date1}/{date2} | 날짜별 기록 조회 |
| 5 | record | GET | api/records/plate/{plate} | 차량 번호별 기록 조회 |
| 6 | record | PUT | api/records | 기록 수정 |
| 7 | record | DELETE              | api/records | 기록 삭제 |
| 8 | history | GET | api/histories | 수정/삭제 기록 조회 |

### Flask(port:5000)
| ID | Domain | Method | URI | Description |
| --- | --- | --- | --- | --- |
| 3 | record | POST | api/records | AI 모델 번호판 예측 |

## 7. DB 설계(ERD)
![image](https://github.com/JaeMin1130/MiniProject_LicensePlate/assets/98063854/9e282ca5-41af-4036-94a9-0c92d77d85ab)
## 8. 문제 해결
- **Spring Boot**로 **RESTful API** 개발
    - **관련 문제 해결**
        - [**REST API URI 작성 규칙**](https://www.notion.so/REST-API-URI-790f97e2c2cc416c9e76d30c176b2912?pvs=21)
        - [**서비스 구현 시 인터페이스를 구현하는 형태로 하는 이유**](https://www.notion.so/26467c3475cd4750ab4fff6738a92638?pvs=21)
- **Spring Security**와 **JWT**를 활용한 사용자 인증/인가
    - 로그인 시 Access Token, Refresh Token 생성 후 클라이언트에 전달
    - 인증이 필요한 API 호출 시, Access Token 검중
    - Access Token 만료 시, Refresh Token 검증 후 Access Token 재발급
    - **관련 문제 해결**
        - [JwtAuthenticationFilter는 어떤 필터를 상속받아야 할까](https://www.notion.so/JwtAuthenticationFilter-3b885cbabeaf484eb54357613088691c?pvs=21)
        - [Filter **내에서 발생한 예외 처리하기**](https://www.notion.so/Filter-722dd2b5473643ddaca28b8075a364db?pvs=21)
        - [API별 접근 권한 설정 시 주의할 점](https://www.notion.so/API-6520b5a5c5084d49894fe51cd855d2b6?pvs=21)
        - [특정 API가 SecurityFilterChain을 안 거치게 하는 방법](https://www.notion.so/API-SecurityFilterChain-63cf54dcbe7849389328730aef5afd96?pvs=21)
- **Flask**로 AI model 연동 후 Spring Boot 서버와 통신
    - **WebClient**로 Flask 서버에 HTTP 요청(POST) 전송
    - **관련 문제 해결**
        - [**RestTemplate과 WebClient**](https://www.notion.so/RestTemplate-WebClient-67976b7f91114c0a95dde21703d021b0?pvs=21)
- **AWS RDS**를 활용한 DB 생성, 데이터 모델링
    - **@OneToOne**으로 엔티티 간 일대일 연관관계 설정
    - **@ManyToOne**으로 엔티티 간 다대일 연관관계 설정
        - **@OnDeletion**으로 부모 인스턴스 삭제 시 자식 인스턴스도 함께 삭제
    - 설계한 스키마를 **ERD**로 시각화
    - **관련 문제 해결**
        - [**양방향 연관관계에서의 주의할 점( N:1 )**](https://www.notion.so/N-1-28a5b829a62747758573e697ed32d967?pvs=21)
        - [엔티티 관계 설정(ERD)](https://www.notion.so/ERD-6a48dd3bd4d14d3ebe6d4ac4cbc15d93?pvs=21)

- **AWS S3**의 버킷에 파일 업로드, 파일 이동, 파일 삭제 로직 구현
    - 관리자가 번호판 수정 시, 모델 재학습을 위해 차량 파일은 relearn 폴더로 이동시키고, 잘못 예측한 번호판 파일은 삭제
- springdoc-openapi의 **Swagger** API를 활용한 API 명세 생성
    - [Swagger-UI API 명세 보러 가기](https://github.com/JaeMin1130/MiniProject_LicensePlate/blob/back_spring/docs/APIDocument.pdf)
    - **관련 문제 해결**
        - [SpringBoot 3.x 버전 이상 프로젝트에 Swagger 적용 오류 해결 ](https://www.notion.so/SpringBoot-3-x-Swagger-ecc3efeae07c46a0b645803e70f04587?pvs=21)
## 9. 시퀀스 다이어그램
![image](https://github.com/JaeMin1130/MiniProject_LicensePlate/assets/98063854/0f1cec48-5445-4b6a-8285-67109799cc9a)

## 10. UML(Class Diagram)
![image](https://github.com/EUNJIN6131/MiniProject_LicensePlate/assets/98063854/7928338d-5949-4158-8b05-b5059c61c8cc)
