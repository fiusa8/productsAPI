FROM openjdk:8-jdk-alpine
MAINTAINER GSD
LABEL description="springboot-seed"

RUN ["mkdir", "-p", "/opt/app"]
WORKDIR /opt/app

COPY ["target/*.jar", "application.jar"]

#VOLUME /tmp
#ARG DEPENDENCY=target/dependency
#COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY ${DEPENDENCY}/META-INF /app/META-INF
#COPY ${DEPENDENCY}/BOOT-INF/classes /app

EXPOSE 8888

ENTRYPOINT ["java", "-Dspring.data.mongodb.uri=mongodb://mongodb:27017/test", "-jar", "application.jar"]