FROM java:8
ADD target/*.jar app.jar
VOLUME /tmp
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=common-prod,prod"]