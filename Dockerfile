FROM openjdk:11
WORKDIR /soundy/
COPY . .
COPY ./build/libs/soundy-1.0-all.jar /soundy/soundy-bot.jar
EXPOSE 80
EXPOSE 443
#production token
ENV DISCORD_TOKEN=NTY4NDk0MTA5NDQ3MjkwODkw.XXV67g.RslY9y8iaP7goYu-b3gGOQqI4x8

#testing token
#ENV DISCORD_TOKEN=NTY4NDk0MTA5NDQ3MjkwODkw.XXV67g.RslY9y8iaP7goYu-b3gGOQqI4x8

ENV OWNER=301780655019130880
ENV SENTRY_DSN=https://e3cc36f41d8f489982da1df7d8556739@sentry.io/1537949
ENV YOUTUBE_KEY=AIzaSyB7gKqm8NOkuh2oR-ZDAXTk04iBO7baM2I
ENV PREFIX=,    

CMD ["/bin/sh", "-c", "java $JAVA_OPTIONS -jar soundy-bot.jar"]