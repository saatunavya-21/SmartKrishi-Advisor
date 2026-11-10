# Stage 1: Build the application with Maven and Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build jar
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Minimal runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy built JAR from builder stage
COPY --from=build /app/target/smart-krishi-advisor-0.0.1-SNAPSHOT.jar app.jar

# Expose standard port
EXPOSE 8080

# Run Spring Boot app with dynamic PORT environment variable (required by Render & Railway)
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -Djava.security.egd=file:/dev/./urandom -jar app.jar"]

