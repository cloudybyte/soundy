package net.cloudybyte.bot.commands.owner;

import com.mongodb.*;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.core.premiumcheck.PremiumTierCache;
import net.cloudybyte.bot.core.premiumcheck.PremiumTiers;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.lang.reflect.Array;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class addtocustomerdb implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if(args.isEmpty()){
            event.getChannel().sendMessage("no args!").queue();
            return;
        }
        if (args.get(0).equals("cache")){

            PremiumTierCache.customerCache.put(event.getAuthor().getId(), PremiumTiers.TIER2);
            return;
        }


        MongoClient mongoClient = null;
        mongoClient = new MongoClient(new MongoClientURI(Constants.DBUri));
        DB database = mongoClient.getDB("soundy");
        DBCollection collection = database.getCollection("customerDB");

        Set<String> collectionNames = database.getCollectionNames();
        System.out.println(collectionNames.toString());

        //search for guild in db
        DBObject query = new BasicDBObject("customerID", args.get(0));
        DBCursor cursor = collection.find(query);

        //update value
        collection.update(new BasicDBObject("discordID", event.getAuthor().getId()), new BasicDBObject("$set", new BasicDBObject("customerID", args.get(0))), true, false);



    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getInvoke() {
        return "addtodb";
    }
}
