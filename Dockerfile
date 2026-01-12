# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml first to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source and build
COPY src ./src
# build with a specific final name to avoid wildcard issues
RUN mvn clean package -DskipTests -DfinalName=app

# Stage 2: Run
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
# Copy the specific file named 'app.jar'
COPY --from=build /app/target/app.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]