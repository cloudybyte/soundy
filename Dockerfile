FROM maslick/minimalka:jdk11
WORKDIR /app
COPY soundy-1.0-all.jar ./soundy.jar
CMD java $JAVA_OPTIONS -jar app.jar