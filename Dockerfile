# syntax=docker/dockerfile:1.6

FROM eclipse-temurin:17-jdk AS builder
WORKDIR /workspace

COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -q -B dependency:go-offline

COPY src src
RUN ./mvnw -q -B -DskipTests package && \
    cp target/*.jar app.jar

FROM eclipse-temurin:17-jre
WORKDIR /app

RUN groupadd --system spring && useradd --system --gid spring spring
USER spring

COPY --from=builder /workspace/app.jar /app/app.jar

ENV JAVA_OPTS=""
ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
