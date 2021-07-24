FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/shiro-plus-loader-admin-server-1.0.0-RELEASE.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]