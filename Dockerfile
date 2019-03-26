FROM openjdk:8-jdk-alpine
MAINTAINER GSD
LABEL description="springboot-seed"

RUN ["mkdir", "-p", "/opt/app"]
WORKDIR /opt/app

ADD . /code/
COPY ["target/*.jar", "application.jar"]

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.data.mongodb.uri=mongodb://mongodb:27017/test", "-jar", "application.jar"]