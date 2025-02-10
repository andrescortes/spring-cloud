FROM gradle:8.11-jdk17-alpine AS builder
LABEL authors="andres"

WORKDIR /usr/app
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle build --no-daemon -x test --info --stacktrace

FROM amazoncorretto:17-alpine
ENV APP_HOME=/usr/app
ENV JAVA_OPTS="-Xms128m -Xmx512m"
ENV JAR_FILE=eureka-server-0.0.1-SNAPSHOT.jar
WORKDIR $APP_HOME
COPY --from=builder $APP_HOME/build/libs/$JAR_FILE .
EXPOSE 8761

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar $JAR_FILE"]