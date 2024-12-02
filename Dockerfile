# Use official OpenJDK image as base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Gradle wrapper files
COPY gradlew gradlew.bat /app/
COPY gradle /app/gradle

# Convert gradlew line endings to LF and set execute permission
RUN dos2unix gradlew && chmod +x gradlew

# Copy the source code into the container
COPY . /app

# Install dependencies (including dos2unix to handle line endings)
RUN apt-get update && apt-get install -y wget unzip dos2unix && \
    wget https://services.gradle.org/distributions/gradle-8.8-bin.zip -P /tmp && \
    unzip /tmp/gradle-8.8-bin.zip -d /opt && \
    rm /tmp/gradle-8.8-bin.zip && \
    ln -s /opt/gradle-8.8/bin/gradle /usr/bin/gradle

# Install file command (optional)
RUN apt-get install -y file

# Use gradle wrapper to build the application
RUN ./gradlew build -x test

# Copy the jar file from the build directory
COPY build/libs/*.jar app.jar

# Expose port 8080
EXPOSE 9090

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]