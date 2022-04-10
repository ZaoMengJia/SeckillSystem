FROM java:8
ADD target/*.jar app.jar
VOLUME /tmp
EXPOSE 8001
ENTRYPOINT ["java", "-jar", "app.jar"]