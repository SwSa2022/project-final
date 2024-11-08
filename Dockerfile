FROM openjdk:17-jdk-slim

ENV JAVA_OPTS=""

COPY target/JiraRush-1.0.jar /app/JiraRush-1.0.jar
COPY resources /app/resources
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/JiraRush-1.0.jar", "--spring.profiles.active=prod"]

