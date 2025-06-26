FROM openjdk:25-jdk
WORKDIR /app

COPY .env .env

COPY ./src/main/resources/db/migrations /app/db/migrations

COPY /target/greenimpact-0.0.1-SNAPSHOT.jar application.jar

ENTRYPOINT ["java", "-jar","application.jar"]

EXPOSE 8080
