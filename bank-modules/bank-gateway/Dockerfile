FROM java:8
ADD target/*.jar app.jar
VOLUME /tmp
EXPOSE 8888
ENTRYPOINT ["java", "-Xmx256m", "-Xmx256m", "-jar", "app.jar", "--spring.profiles.active=common-prod,prod"]