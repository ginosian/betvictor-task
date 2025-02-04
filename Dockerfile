FROM gradle:8.0-jdk17 AS builder
WORKDIR /home/gradle/project

ENV GRADLE_USER_HOME /home/gradle/.gradle

RUN mkdir -p /home/gradle/.gradle && chmod -R 777 /home/gradle/.gradle

COPY --chown=gradle:gradle . .

RUN gradle --stop && gradle --no-daemon --stacktrace --info clean build -x test --refresh-dependencies

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

COPY variables.env /app/variables.env

COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/app/entrypoint.sh"]
