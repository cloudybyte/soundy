package net.cloudybyte.bot.listeners;


import com.mongodb.*;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.cloudybyte.bot.util.Colors;
import net.cloudybyte.bot.util.EmbedUtil;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GuildLeaveListener extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(GuildLeaveListener.class);
    SentryClient sentry = SentryClientFactory.sentryClient();
    private String GREEN = Colors.GREEN;
    private String RESET = Colors.RESET;
    EmbedUtil embedUtil =  new EmbedUtil();



    @lombok.SneakyThrows
    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        //remove entries from db
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        DB database = mongoClient.getDB("soundy");
        DBCollection volumeCollection = database.getCollection("volumes");
        DBCollection loopingCollection = database.getCollection("trackLooping");
        long guildid = event.getGuild().getIdLong();
        volumeCollection.remove(new BasicDBObject("guildid", guildid));
        loopingCollection.remove(new BasicDBObject("guildid", guildid));
        event.getGuild().getOwner().getUser().openPrivateChannel().queue(privateChannel ->
                privateChannel.sendMessage(embedUtil.smallinfo("Oww :(", "We're sorry that soundy couldn't fulfill your expectations! If there is anything you think we could do better please let us know in our support guild at https://discord.gg/HXXAqwb")).queue());
    }
}

