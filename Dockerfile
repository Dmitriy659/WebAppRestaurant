FROM openjdk:21
ADD target/Restaurant-1.jar app.jar
ENTRYPOINT ["java", "-jar","app.jar"]
EXPOSE 8080