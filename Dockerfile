FROM gradle:jdk11 AS builder

LABEL maintainer="Nicholas Dietz @ Fellow-Flow"
WORKDIR /app
COPY build.gradle.kts settings.gradle ./
COPY src/ src/
RUN gradle --no-daemon build --stacktrace -x test


FROM gcr.io/distroless/java:11

WORKDIR /user-service
COPY --from=builder /app/build/libs/*.jar ./app.jar
EXPOSE 8081
CMD [ "-Xmx256m", "-Xss32m", "-Djava.security.egd=file:/dev/./urandom","./app.jar"]
