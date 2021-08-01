FROM openjdk:8-alpine
RUN addgroup -S shiroplus && adduser -S shiroplus -G shiroplus
USER shiroplus:shiroplus
RUN mvn clean package -Dmaven.test.skip=true -U
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]