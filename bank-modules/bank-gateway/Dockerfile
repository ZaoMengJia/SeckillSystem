FROM java:8
ADD target/*.jar app.jar
VOLUME /tmp
EXPOSE 8811
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=common-prod,prod"]