# Use official OpenJDK image as base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Accept build argument to skip tests
ARG SKIP_TESTS=false

# Copy the Gradle wrapper files
COPY gradlew gradlew.bat /app/
COPY gradle /app/gradle

# Install dos2unix to convert line endings (in case gradlew has Windows line endings)
RUN apt-get update && apt-get install -y dos2unix && \
    dos2unix gradlew && \
    chmod +x gradlew

# Copy the source code into the container
COPY . /app

# Use gradle wrapper to build the application (Gradle will be downloaded by gradlew)
RUN ./gradlew build ${SKIP_TESTS} -x test

# Copy the jar file from the build directory
COPY build/libs/*.jar app.jar

# Expose port 8080
EXPOSE 9090

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
