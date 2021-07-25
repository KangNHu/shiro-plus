FROM openjdk:jdk-8-alpine AS maven_build

COPY pom.xml /tmp/

COPY src /tmp/src/

WORKDIR /tmp/

RUN mvn package

FROM openjdk:8

#expose port 8080
EXPOSE 8080

#default command
CMD java -jar /data/${jarName}.jar

#copy  docker image from builder image

COPY --from=maven_build /tmp/target/${jarName}.jar /data/${jarName}.jar