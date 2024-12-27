# Use OpenJDK 21 slim image
FROM openjdk:21-slim

# Set working directory
WORKDIR /app

# Copy the built JAR file
COPY target/test-gen-service-0.0.1-SNAPSHOT.jar app.jar

# Expose application port
EXPOSE 8064

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
