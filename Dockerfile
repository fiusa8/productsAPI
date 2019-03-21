FROM openjdk:8-jdk-alpine
MAINTAINER GSD
LABEL description="springboot-seed"

RUN ["mkdir", "-p", "/opt/app"]
WORKDIR /opt/app

COPY ["target/*.jar", "application.jar"]

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "application.jar"]