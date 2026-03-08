# syntax=docker/dockerfile:1.7

# Build and run on Java 21 to match the current Gradle toolchain and class target.
FROM bellsoft/liberica-openjdk-alpine:21 AS build

WORKDIR /app

COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle
RUN chmod +x ./gradlew

COPY src ./src
COPY data ./data

RUN ./gradlew --no-daemon bootJar

FROM bellsoft/liberica-openjre-alpine:21

WORKDIR /app

ENV JAVA_OPTS="-XX:+UseSerialGC -Xms256m -Xmx384m -Xss512k"
ENV SPRING_PROFILES_ACTIVE=prod

COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
