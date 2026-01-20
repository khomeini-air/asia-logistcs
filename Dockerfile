# Stage 1
FROM eclipse-temurin:17-jdk-focal AS builder
WORKDIR /app

# Copy the Gradle Wrapper files
COPY gradlew .
COPY gradle ./gradle

# Copy the build configuration files
COPY build.gradle settings.gradle ./

# Download dependencies (Cache)
RUN ./gradlew --no-daemon dependencies

# Copy the source code
COPY src ./src

# Build the application
RUN ./gradlew clean bootJar -x test --no-daemon

# Stage 2
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Copy the built JAR from Stage 1
COPY --from=builder /app/build/libs/asia-logistics.jar app.jar

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
