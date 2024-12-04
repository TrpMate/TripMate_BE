# Use official OpenJDK image as base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the specific JAR file to the container
COPY build/libs/trip-mate-0.0.1-SNAPSHOT.jar app.jar

# Expose port 9090
EXPOSE 9090

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]