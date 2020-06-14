package net.cloudybyte.bot.util;

import com.mongodb.*;
import net.cloudybyte.bot.core.Constants;
import net.dv8tion.jda.api.entities.Guild;

import java.net.UnknownHostException;


public class GuildVolumeHandler {

    public void setVolume(Guild guild, Integer volume) throws UnknownHostException {

        MongoClient mongoClient = new MongoClient(new MongoClientURI(Constants.DBUri));
        DB database = mongoClient.getDB("soundy");
        DBCollection collection = database.getCollection("volumes");
        long guildid = guild.getIdLong();

        //search for guild in db
        DBObject query = new BasicDBObject("guildid", guildid);
        DBCursor cursor = collection.find(query);

        //update value
        collection.update(new BasicDBObject("guildid", guildid), new BasicDBObject("$set", new BasicDBObject("volume", volume)), true, false);

        mongoClient.close();

    }

}