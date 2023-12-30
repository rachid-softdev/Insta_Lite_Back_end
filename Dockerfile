# Étape 1 : Construction du WAR
FROM maven:3.8.4-openjdk-17-slim AS builder
WORKDIR /app
COPY . .
RUN mvn compile
RUN mvn clean package -DskipTests

# Étape 2 : Construction de l'image Docker
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=/app/target/instalite-0.0.1-SNAPSHOT.war
COPY --from=builder ${JAR_FILE} ./target/application.war
ENTRYPOINT ["java", "-jar", "target/application.war"]
