FROM openjdk:11-jre-slim
MAINTAINER keydo.tistory.com
ARG JAR_FILE=./jar/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
