# Use official OpenJDK image as base image
FROM openjdk:17-jdk-slim

# Install Gradle (Optional, if you need to build inside Docker)
RUN apt-get update && apt-get install -y wget unzip && \
    wget https://services.gradle.org/distributions/gradle-8.8-bin.zip -P /tmp && \
    unzip /tmp/gradle-8.8-bin.zip -d /opt && \
    rm /tmp/gradle-8.8-bin.zip && \
    ln -s /opt/gradle-8.8/bin/gradle /usr/bin/gradle

# Set the working directory
WORKDIR /app

# Copy the Gradle wrapper files
COPY gradlew gradlew.bat /app/
COPY gradle /app/gradle

# Make the Gradle wrapper executable
RUN chmod +x gradlew

# Copy the source code into the container
COPY . /app

# Conditional build logic to skip tests
ARG SKIP_TESTS=false
RUN if [ "$SKIP_TESTS" = "true" ]; then ./gradlew clean build -x test; else ./gradlew clean build; fi

# Copy the jar file from the build directory
COPY build/libs/*.jar app.jar

# Expose port 8080
EXPOSE 9090

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]