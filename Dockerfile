FROM openjdk:17-jdk-slim

ENV JAVA_OPTS=""
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} JiraRush-1.0.jar
COPY resources /app/resources
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/JiraRush-1.0.jar"]
#USER root


