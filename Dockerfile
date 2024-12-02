# Use official OpenJDK image as base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Gradle wrapper files
COPY gradlew gradlew.bat /app/
COPY gradle /app/gradle

# Make the Gradle wrapper executable
RUN chmod +x gradlew

# Copy the source code into the container
COPY . /app

# Install Gradle (Optional, if you need to build inside Docker)
RUN apt-get update && apt-get install -y wget unzip && \
    wget https://services.gradle.org/distributions/gradle-8.8-bin.zip -P /tmp && \
    unzip /tmp/gradle-8.8-bin.zip -d /opt && \
    rm /tmp/gradle-8.8-bin.zip && \
    ln -s /opt/gradle-8.8/bin/gradle /usr/bin/gradle

# file 명령어를 사용하기 위해 필요한 패키지 설치 (apt 사용)
RUN apt-get install -y file

# gradlew 파일이 CRLF인지 확인하고, CRLF이면 LF로 변환
RUN if file gradlew | grep -q CRLF; then \
    echo "Converting CRLF to LF in gradlew"; \
    sed -i 's/\r$//' gradlew; \
  else \
    echo "gradlew already uses LF"; \
  fi

RUN ./gradlew build -x test

# Copy the jar file from the build directory
COPY build/libs/*.jar app.jar

# Expose port 8080
EXPOSE 9090

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
