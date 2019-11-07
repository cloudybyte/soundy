# Openjdk 11 required
FROM openjdk:11
# copy WAR into image
COPY nightplay.jar /nightplay.jar
# run application with this command line
CMD ["/usr/bin/java", "-jar", "-Dspring.profiles.active=default", "/app.war"]