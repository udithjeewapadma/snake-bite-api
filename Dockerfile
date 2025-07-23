# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jdk-focal

# Set working directory inside container
WORKDIR /app

# Copy the built jar file into the container
COPY target/snake-bite-api-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on (default 8080)
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
