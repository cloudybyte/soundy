package net.cloudybyte.bot.commands.music;

import com.mongodb.*;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.audio.GuildMusicManager;
import net.cloudybyte.bot.core.audio.PlayerManager;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.cloudybyte.bot.util.Reactions;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;
import java.util.NoSuchElementException;

import static net.cloudybyte.bot.util.Colors.RED;
import static net.cloudybyte.bot.util.Colors.RESET;

public class ShuffleCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        //TODO PREMIUM CHECK

        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
        EmbedBuilder embedBuilder = new EmbedBuilder();


        MongoClient mongoClient = null;
        mongoClient = new MongoClient(new MongoClientURI(Constants.DBUri));


        DB database = mongoClient.getDB("soundy");
        DBCollection collection = database.getCollection("shuffle");
        String guildid = event.getGuild().getId();

        try {


            //search for guild in db
            DBObject query = new BasicDBObject("guildid", guildid);
            BasicDBObject select = new BasicDBObject();
            select.put("shuffled", 1);
            DBCursor cursor = collection.find(query, select);
            BasicDBObject obj = (BasicDBObject) cursor.next();


            /*
             *   Values and meanings:
             *   0 -> not looped
             *   1 -> track looped
             *   2 -> queue looped
             */

            try {

                try {
                    System.out.println("obj.getString(\"shuffled\") = " + obj.getString("shuffled"));
                    Integer shuffled = Integer.parseInt(obj.getString("shuffled"));

                    System.out.println("shuffled = " + shuffled);
                    System.out.println("3");


                    if (args.isEmpty()) {
                        switch (shuffled) {
                            case 0:
                                embedBuilder.info(event, "Shuffle Info", "Shuffle is turned off right now");
                                break;
                            case 1:
                                embedBuilder.info(event, "Shuffle Info", "Let's play a guessing game about the next song!");
                                break;

                        }
                        return;
                    }

                    if ((shuffled.equals(0))) {
                        collection.update(new BasicDBObject("guildid", guildid), new BasicDBObject("$set", new BasicDBObject("shuffled", 1)), true, false);
                        event.getMessage().addReaction(Reactions.SHUFFLE).queue();

                    } else if (shuffled.equals(1)) {
                        collection.update(new BasicDBObject("guildid", guildid), new BasicDBObject("$set", new BasicDBObject("shuffled", 0)), true, false);
                        event.getMessage().addReaction(Reactions.ARROW_FORWARD).queue();

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
                    System.out.println("obj.getString(\"shuffled\") = " + obj.getString("shuffled"));
                    Integer shuffled = Integer.parseInt(obj.getString("shuffled"));

                    System.out.println("shuffled = " + shuffled);
                    System.out.println("3");
                    if (args.isEmpty()) {
                        switch (shuffled) {
                            case 0:
                                embedBuilder.info(event, "Shuffle Info", "Shuffle is turned off right now");
                                break;
                            case 1:
                                embedBuilder.info(event, "Shuffle Info", "Let's play a guessing game about the next song!");
                                break;

                        }
                        return;
                    }

                    System.out.println("4");

                    if ((shuffled.equals(0))) {
                        collection.update(new BasicDBObject("guildid", guildid), new BasicDBObject("$set", new BasicDBObject("shuffled", 1)), true, false);
                        event.getMessage().addReaction(Reactions.SHUFFLE).queue();

                    } else if (shuffled.equals(1)) {
                        collection.update(new BasicDBObject("guildid", guildid), new BasicDBObject("$set", new BasicDBObject("shuffled", 0)), true, false);
                        event.getMessage().addReaction(Reactions.ARROW_FORWARD).queue();

                    }

                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.out.println(RED + "ARRAY OUT OF BOUNDS EXCEPTION" + RESET);
                }
            } catch (NullPointerException ex) {
                collection.update(new BasicDBObject("guildid", guildid), new BasicDBObject("$set", new BasicDBObject("shuffled", 0)), true, false);
                embedBuilder.info(event, "Shuffle Info", "Shuffle is turned off right now");
            }
        }


        if(guildMusicManager.player.getPlayingTrack() == null) {

        }
    }

    @Override
    public String getHelp() {
        return "Shuffles the queue";
    }

    @Override
    public String getInvoke() {
        return "shuffle";
    }
}
