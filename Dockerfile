# syntax=docker/dockerfile:1.7

# Java bytecode is platform-neutral, so compile on the native GitHub runner
# instead of emulating ARM64. The final image still targets the requested platform.
FROM --platform=$BUILDPLATFORM bellsoft/liberica-openjdk-alpine:21 AS build

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
