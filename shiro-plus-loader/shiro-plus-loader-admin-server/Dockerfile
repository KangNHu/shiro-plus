FROM openjdk:8-alpine
RUN addgroup -S shiroplus && adduser -S shiroplus -G shiroplus
USER shiroplus:shiroplus
RUN chown -R 777 shiroplus:shiroplus /usr/local

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar","&"]