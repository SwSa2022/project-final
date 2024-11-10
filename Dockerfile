FROM nginx:alpine

# Копируем конфигурацию NGINX
COPY ./config/nginx.conf /etc/nginx/nginx.conf
COPY ./config/default.conf /etc/nginx/conf.d/default.conf

# Копируем статические ресурсы в контейнер
COPY ./resources/static /usr/share/nginx/html/static

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

FROM openjdk:17-jdk-slim

COPY target/JiraRush-1.0.jar ./JiraRush-1.0.jar
COPY resources ./resources
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/JiraRush-1.0.jar", "--spring.profiles.active=prod"]

