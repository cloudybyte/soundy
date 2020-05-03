package net.cloudybyte.bot.commands.music;

import com.mongodb.*;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.cloudybyte.bot.util.Reactions;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static net.cloudybyte.bot.util.Colors.*;

public class LoopCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        SentryClient sentry = SentryClientFactory.sentryClient();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        long guildid = event.getGuild().getIdLong();

        System.out.println("1");

        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient(new MongoClientURI(Constants.DBUri));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }




/*        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(this.username, this.dbname,
                this.password.toCharArray());
        MongoClient mongoClient = new MongoClient(new ServerAddress(dbhost, this.dbport), Arrays.asList(mongoCredential));

 */
        DB database = mongoClient.getDB("soundy");
        DBCollection collection = database.getCollection("trackLooping");


        try {

            //search for guild in db
            DBObject query = new BasicDBObject("guildid", guildid);
            BasicDBObject select = new BasicDBObject();
            select.put("looped", 1);
            DBCursor cursor = collection.find(query, select);
            System.out.println("Constants.DBUri = " + Constants.DBUri);
            BasicDBObject obj = (BasicDBObject) cursor.next();


            /*
             *   Values and meanings:
             *   0 -> not looped
             *   1 -> track looped
             *   2 -> queue looped
             */

            System.out.println("2");

            try {

                try {
                    System.out.println("obj.getString(\"looping\") = " + obj.getString("looped"));
                    Integer looping = Integer.parseInt(obj.getString("looped"));

                    System.out.println("looping = " + looping);
                    System.out.println("3");
                    if (args.isEmpty()) {
                        if (looping.equals(0)) {
                            embedBuilder.info(event, "Looping Info", "Looping is turned off right now");
                        } else if (looping.equals(1)) {
                            embedBuilder.info(event, "Looping Info", "I am looping the current song");
                        } else if (looping.equals(2)) {
                            embedBuilder.info(event, "Looping Info", "I am looping the queue");
                        }
                        return;
                    }
                    System.out.println("4");

                    if ((looping.equals(0) || looping.equals(1)) && args.get(0).equals("queue")) {
                        collection.update(new BasicDBObject("guildid", guildid), new BasicDBObject("$set", new BasicDBObject("looped", 2)), true, false);
                        event.getMessage().addReaction(Reactions.REPEAT_QUEUE).queue();

                    } else if (looping.equals(2) && args.get(0).equals("queue")) {
                        collection.update(new BasicDBObject("guildid", guildid), new BasicDBObject("$set", new BasicDBObject("looped", 0)), true, false);
                        event.getMessage().addReaction(Reactions.ARROW_FORWARD).queue();

                    } else if (args.get(0).equals("off")) {
                        collection.update(new BasicDBObject("guildid", guildid), new BasicDBObject("$set", new BasicDBObject("looped", 0)), true, false);
                        event.getMessage().addReaction(Reactions.ARROW_FORWARD).queue();

                    } else if (looping.equals(1) && args.get(0).equals("song")) {
                        collection.update(new BasicDBObject("guildid", guildid), new BasicDBObject("$set", new BasicDBObject("looped", 0)), true, false);
                        event.getMessage().addReaction(Reactions.ARROW_FORWARD).queue();

                    } else if ((looping.equals(2) || looping.equals(0)) && args.get(0).equals("song")) {
                        collection.update(new BasicDBObject("guildid", guildid), new BasicDBObject("$set", new BasicDBObject("looped", 1)), true, false);
                        event.getMessage().addReaction(Reactions.REPEAT_ONE).queue();
                    }

                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(RED + "ARRAY OUT OF BOUNDS EXCEPTION" + RESET);
                }
            } catch (NullPointerException e) {
                collection.update(new BasicDBObject("guildid", guildid), new BasicDBObject("$set", new BasicDBObject("looped", 0)), true, false);
                embedBuilder.info(event, "Looping Info", "Looping is turned off right now");
            }
        }catch (NoSuchElementException e){
            //Insert element
            collection.update(new BasicDBObject("guildid", guildid), new BasicDBObject("$set", new BasicDBObject("looped", 0)), true, false);
            //search for guild in db
            DBObject query = new BasicDBObject("guildid", guildid);
            BasicDBObject select = new BasicDBObject();
            select.put("looped", 1);
            DBCursor cursor = collection.find(query, select);
            BasicDBObject obj = (BasicDBObject) cursor.next();


            try {

                try {
                    System.out.println("obj.getString(\"looping\") = " + obj.getString("looped"));
                    Integer looping = Integer.parseInt(obj.getString("looped"));

                    System.out.println("looping = " + looping);
                    System.out.println("3");
            if (args.isEmpty()) {
                if (looping.equals(0)) {
                    embedBuilder.info(event, "Looping Info", "Looping is turned off right now");
                } else if (looping.equals(1)) {
                    embedBuilder.info(event, "Looping Info", "I am looping the current song");
                } else if (looping.equals(2)) {
                    embedBuilder.info(event, "Looping Info", "I am looping the queue");
                }
                return;
            }
            System.out.println("4");

            if ((looping.equals(0) || looping.equals(1)) && args.get(0).equals("queue")) {
                collection.update(new BasicDBObject("guildid", guildid), new BasicDBObject("$set", new BasicDBObject("looped", 2)), true, false);
                event.getMessage().addReaction(Reactions.REPEAT_QUEUE).queue();

            } else if (looping.equals(2) && args.get(0).equals("queue")) {
                collection.update(new BasicDBObject("guildid", guildid), new BasicDBObject("$set", new BasicDBObject("looped", 0)), true, false);
                event.getMessage().addReaction(Reactions.ARROW_FORWARD).queue();

            } else if (args.get(0).equals("off")) {
                collection.update(new BasicDBObject("guildid", guildid), new BasicDBObject("$set", new BasicDBObject("looped", 0)), true, false);
                event.getMessage().addReaction(Reactions.ARROW_FORWARD).queue();

            } else if (looping.equals(1) && args.get(0).equals("song")) {
                collection.update(new BasicDBObject("guildid", guildid), new BasicDBObject("$set", new BasicDBObject("looped", 0)), true, false);
                event.getMessage().addReaction(Reactions.ARROW_FORWARD).queue();

            } else if ((looping.equals(2) || looping.equals(0)) && args.get(0).equals("song")) {
                collection.update(new BasicDBObject("guildid", guildid), new BasicDBObject("$set", new BasicDBObject("looped", 1)), true, false);
                event.getMessage().addReaction(Reactions.REPEAT_ONE).queue();
            }

        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println(RED + "ARRAY OUT OF BOUNDS EXCEPTION" + RESET);
        }
    } catch (NullPointerException ex) {
        collection.update(new BasicDBObject("guildid", guildid), new BasicDBObject("$set", new BasicDBObject("looped", 0)), true, false);
        embedBuilder.info(event, "Looping Info", "Looping is turned off right now");
    }
        }

    }


    @Override
    public String getHelp() {
        return "lets you loop the playing song";
    }

    @Override
    public String getInvoke() {
        return "loop";
    }

}