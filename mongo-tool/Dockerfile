FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD mongo-tool-1.0.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
EXPOSE 8090
