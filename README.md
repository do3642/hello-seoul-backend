# 안녕, 서울 (Hello, Seoul) - Backend

## 📚 프로젝트 개요

이 프로젝트는 **서울 관광지 소개 사이트**를 개발하는 프로젝트입니다.  
사용자들에게 서울의 주요 관광지, 축제를 소개하고, 관광지에 대한 정보를 각 구별, 검색을 통해 제공합니다.  
프로젝트는 **React**와 **Spring Boot**를 사용하여 프론트엔드와 백엔드를 구현합니다.

## ⏳ 작업 기간 및 내용

- **작업 기간**: 2024년 12월 31일 ~ 2025년 1월 22일
- **내용**:
  - 서울의 주요 관광지 정보 제공
  - 계절별 맞춤 관광지 추천 기능 구현
  - 지하철역에 따른 관광지 추천 제안
  - 진행중인 축제 관련 정보 제공

## 💡 기능 소개

- **서울 관광지 목록**: 서울의 주요 관광지 리스트를 확인할 수 있습니다.
- **관광지 상세 정보**: 각 관광지에 대한 상세 정보를 제공합니다.
- **검색 기능**: 특정 관광지를 검색할 수 있습니다.
- **추천 시스템**: 계절별 관광지를 추천해주는 기능을 제공합니다.

## 🔧 기술 스택

- **프론트엔드**: React, Axios
- **백엔드**: Spring Boot, MySQL

## 👨‍💻 팀 구성

이 프로젝트는 총 4명이 참여한 팀 프로젝트입니다:
- **이영찬** (팀 리더)
- **최하영**
- **권민수**
- **허경**

## 설치 및 실행 방법

### 1. **백엔드 실행**

#### 1.1 **프로젝트 클론**
먼저, GitHub에서 백엔드 프로젝트를 클론합니다.

  ```bash
  git clone https://github.com/your-username/hello-seoul-backend.git
  ```

#### 1.2 **STS에서 프로젝트 열기**
1. STS(Spring Tool Suite)를 실행합니다.
2. `File` -> `Import`를 클릭합니다.
3. `Existing Gradle Project`를 선택한 후 `Next`를 클릭합니다.
4. `Root Directory`에서 `hello-seoul-backend` 프로젝트를 선택하고 `Finish`를 클릭합니다.

#### 1.3 **`application.properties` 설정**
프로젝트의 `src/main/resources` 폴더에 `application.properties` 파일을 위치시킵니다. 아래 내용을 추가합니다.

```bash
spring.application.name=helloseoul

# 서버 설정
server.port=8888
server.servlet.context-path=/
server.servlet.encoding.charset=UTF-8

# DB 설정
spring.datasource.url=jdbc:mysql://localhost:3306/helloseoul?useSSL=false&serverTimezone=Asia/Seoul
spring.datasource.username=root
spring.datasource.password=1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA 설정 
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# 쿼리 로그 출력 설정
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.show_sql=true
```

#### 1.4 **MySQL 데이터베이스 생성**
1. MySQL을 실행하고, `helloseoul`이라는 이름의 데이터베이스를 생성합니다.

CREATE DATABASE helloseoul;

#### 1.5 **프로젝트 빌드 및 실행**
STS에서 프로젝트를 빌드하고 실행하려면, 다음 단계를 따릅니다:

1. **프로젝트 빌드**:
   - STS에서 `build.gradle` -> `Refresh Gradle Project`를 선택하여 Gradle 프로젝트를 업데이트합니다.

2. **애플리케이션 실행**:

서버가 성공적으로 실행되면, 기본적으로 `http://localhost:8888`에서 백엔드 서버가 실행됩니다.
