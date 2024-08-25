# Use the official Maven image to build the app
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Use the official OpenJDK image to run the app
FROM openjdk:17-jdk-slim

# Install redis-tools for redis-cli and iputils-ping for ping
RUN apt-get update && apt-get install -y \
    redis-tools \
    iputils-ping \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=build /app/target/cardcost-api-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.yml /app/application.yml

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
