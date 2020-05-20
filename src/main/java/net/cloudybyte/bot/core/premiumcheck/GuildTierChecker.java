package net.cloudybyte.bot.core.premiumcheck;

import com.mongodb.*;
import net.cloudybyte.bot.core.Constants;

import java.util.NoSuchElementException;

public class GuildTierChecker {
    public int getGuildPremiumTier(long guildid) {
        //Connect to DB
        MongoClient mongoClient = null;
        mongoClient = new MongoClient(new MongoClientURI(Constants.DBUri));
        DB database = mongoClient.getDB("soundy");
        DBCollection collection = database.getCollection("boostedGuilds");


        //search for guild in db
        DBObject query = new BasicDBObject("guildid", guildid);
        DBCursor cursor = collection.find(query);
        try {
            BasicDBObject obj = (BasicDBObject) cursor.next();
            Integer tier = Integer.parseInt(obj.getString("tier"));
        } catch (NoSuchElementException ignored) {
            return 0;
        }
        return 0;
    }
}