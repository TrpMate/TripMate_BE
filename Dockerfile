# 1. 빌드 환경 설정 (Gradle 기반 JAR 빌드)
FROM gradle:8-jdk17 AS builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 파일 복사
COPY build.gradle settings.gradle ./
COPY gradle /app/gradle

# 종속성 다운로드
RUN gradle dependencies

# 소스 코드 복사 및 빌드
COPY . /app
RUN gradle build -x test  # 테스트 제외하고 빌드 실행

# 2. 실행 환경 설정 (JAR 파일을 실행하는 환경)
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일을 복사
COPY --from=builder /app/build/libs/trip-mate-0.0.1-SNAPSHOT.jar /app/tripmate-be.jar

# 빌드 시 전달받을 ARG 변수들 설정
ARG DATA_HOST
ARG DATA_USERNAME
ARG DATA_USERPASSWORD
ARG ENCRYPT_KEY

# 환경 변수 설정 (빌드 시 전달된 환경 변수)
ENV data.host=${DATA_HOST}
ENV data.username=${DATA_USERNAME}
ENV data.userpassword=${DATA_USERPASSWORD}
ENV ENCRYPT_KEY=${ENCRYPT_KEY}

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/tripmate-be.jar"]
