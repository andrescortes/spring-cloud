# Build Stage
FROM gradle:8.11-jdk17 AS builder
WORKDIR /usr/app
COPY settings.gradle build.gradle lombok.config ./
COPY src ./src
RUN gradle build --no-daemon -x test --stacktrace --info

# Runtime Stage
FROM amazoncorretto:17-alpine
ENV APP_HOME=/usr/app
ENV JAR_FILE=msvc-items-0.0.1-SNAPSHOT.jar
WORKDIR $APP_HOME
COPY --from=builder $APP_HOME/build/libs/$JAR_FILE .
EXPOSE 9002
ENTRYPOINT ["sh", "-c", "java -jar $JAR_FILE"]