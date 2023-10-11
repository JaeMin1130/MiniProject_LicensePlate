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
![image](https://github.com/EUNJIN6131/MiniProject_LicensePlate/assets/98063854/a3aecb4f-44cc-4274-9fa0-788e230fe504)

## 6. 화면 구성
| 로그인 페이지  |  회원가입 페이지   |
| :-------------------------------------------: | :------------: |
|  <img width="450" height="350"  src="https://github.com/EUNJIN6131/MiniProject_LicensePlate/assets/129818813/0d2a3f2b-4663-4003-a8b1-80bb42acb0b3"/> |  <img width="450" height="350" src="https://github.com/EUNJIN6131/MiniProject_LicensePlate/assets/129818813/398cbe50-a0a7-4272-b3b7-38f3bfe0bc00"/>|  

| 메인 페이지(차량 입출입 현황)  |  메인 페이지(차량 검색)   |  
| :-------------------------------------------: | :------------: |
| <img width="450" height="350" src="https://github.com/EUNJIN6131/MiniProject_LicensePlate/assets/129818813/4d26aa28-a936-4751-97aa-e15f5d81e9b9"/>   |  <img width="450" height="350" src="https://github.com/EUNJIN6131/MiniProject_LicensePlate/assets/129818813/ae1061e6-67dc-4eb9-8afb-db93087a7077"/>|

| 메인 페이지(날짜 조회)  |  메인 페이지(관리자 권한 기능)   |
| :-------------------------------------------: | :------------: |
|  <img width="450" height="350"  src="https://github.com/EUNJIN6131/MiniProject_LicensePlate/assets/129818813/72b93fac-755f-4010-81ba-db38432ae527"/> |  <img width="450" height="350"  src="https://github.com/EUNJIN6131/MiniProject_LicensePlate/assets/129818813/00512f7f-8f9c-411e-8cb2-49cd722987fb"/>|  

| 메인 페이지(수정/삭제 기록)  |
| :-------------------------------------------: |
|  <img width="450" height="350" src="https://github.com/EUNJIN6131/MiniProject_LicensePlate/assets/129818813/adc6aa2d-302f-49c7-a2d4-b6a4ab2dc37c"/> |

| 시연 영상  |
| :-------------------------------------------: | 
|   <img width="900" height="600" src="https://github.com/EUNJIN6131/MiniProject_LicensePlate/assets/129818813/63260f92-db31-4891-8512-ead532c36c1d"> |

## 7. REST API 명세 
### Spring Boot
| ID | Method | URI | Params | Return | Description |
| --- | --- | --- | --- | --- | --- |
| 1 | POST  | /user/signup | UserDto userDto | Boolean | 회원가입 |
| 2 | POST | /user/signin | UserDto userDto | UserDto | 로그인 |
| 3 | POST | /main/record | MultipartFile file | List<LogDto> | 로그 기록 |
| 4 | GET | /main/search/date | Date start, Date end | List<LogDto> | 날짜별 조회 |
| 5 | GET | /main/search/plate | String plate | List<LogDto> | 차량번호별 조회 |
| 6 | GET | /main/history |  | List<HistoryDto> | 수정/삭제 기록 조회 |
| 7 | PUT | /main/update | List<LogDto> list | Boolean | 로그 수정(admin) |
| 8 | DELETE              | /main/delete | List<LogDto> list | Boolean | 로그 삭제(admin) |
### Flask
| ID | Method | URI | Params | Return | Description |
| --- | --- | --- | --- | --- | --- |
| 3 | POST | /main/record | MultipartFile file | LogDto | 로그 기록 |

## 8. DB 설계(ERD)
![image](https://github.com/EUNJIN6131/MiniProject_LicensePlate/assets/98063854/3ac64e86-1ea1-4fbf-a5da-becd6a1e6738)

## 9. 시퀀스 다이어그램
![image](https://github.com/EUNJIN6131/MiniProject_LicensePlate/assets/98063854/06b3d3fe-42a4-4a96-92ff-7e23b56b9064)

## 10. UML(Class Diagram)
![image](https://github.com/EUNJIN6131/MiniProject_LicensePlate/assets/98063854/7928338d-5949-4158-8b05-b5059c61c8cc)

## 11. 개발 일지 
<a href="https://shrub-snap-550.notion.site/CRUD-566be659b7bf4693a6515f408cf2f1d9?pvs=4">개발 일지 보러 가기  <img width="23" src="https://upload.wikimedia.org/wikipedia/commons/e/e9/Notion-logo.svg"> </a>****
