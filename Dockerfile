FROM amazoncorretto:17-alpine
EXPOSE 8080
ADD target/cloudstorage-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]