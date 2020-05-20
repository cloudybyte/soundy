package net.cloudybyte.bot.commands.music.Playlist;

import com.mongodb.*;
import org.bson.Document;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.audio.GuildMusicManager;
import net.cloudybyte.bot.core.audio.PlayerManager;
import net.cloudybyte.bot.core.audio.TrackScheduler;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SavePlaylist implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {


        //TODO CHECK FOR PREMIUM USER


        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (args.isEmpty()) {
            embedBuilder.error(event, "No args provided", "You have to provide a name in order to save the playlist");
            return;
        }
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;
        AudioPlayer player = musicManager.player;

        Collection<AudioTrack> queueList = new LinkedList<>(scheduler.getQueue());


        switch (args.get(0)) {
            //load playlist into queue
            case "l":
            case "load":
                System.out.println("loading triggered!");
                //check for name is empty
                if (args.size() == 1) {
                    embedBuilder.error(event, "Cannot load saved playlist", "You have to provide a name in order to load a playlist");
                    return;
                }

                break;


            // Save playlist to storage
            case "s":
            case "save":
                System.out.println("saving triggered");
                if (args.get(1).isEmpty()) {
                    embedBuilder.error(event, "Cannot save queue to playlist", "You have to provide a name in order to save a playlist");
                    return;
                }


                //Initializing db connection
                MongoClient mongoClient = null;
                mongoClient = new MongoClient(new MongoClientURI(Constants.DBUri));
                DB database = mongoClient.getDB("soundy");
                DBCollection collection = database.getCollection("savedPlaylists");

                //search for user in db
                DBObject query1 = new BasicDBObject("discordID", event.getAuthor().getId());
                DBCursor cursor1 = collection.find(query1);

                if (cursor1.size() == 0) {
                    DBObject userObject = new BasicDBObject("discordID", event.getAuthor().getId());
                    collection.update(userObject, userObject, true, false);
                }
                DBObject query = new BasicDBObject("discordID", event.getAuthor().getId());
                DBCursor cursor = collection.find(query);


                if (cursor.next().containsField(args.get(1))){
                    event.getChannel().sendMessage("there is already a playlist named like this").queue();
                    return;
                }



                //get value


                BasicDBObject setNewFieldQuery = new BasicDBObject().append("$set", new BasicDBObject().append("key", "value"));
                collection.update(new BasicDBObject().append("discordID", event.getAuthor().getId()), setNewFieldQuery);


                //Build DBObject


                DBObject trackObject = new BasicDBObject();


                int i = 0;
                for (AudioTrack track : queueList) {
                    i++;
                    String trackURL = track.getInfo().uri;
                    trackObject.put("track" + i, trackURL);
                }

                DBObject playlistObject = new BasicDBObject("playlistName", args.get(1)).append("tracks", trackObject);
                DBObject userObject = new BasicDBObject("discordID", event.getAuthor().getId()).append("playlists", playlistObject);
                collection.update(new BasicDBObject().append("discordID", event.getAuthor().getId()), userObject, false, false);


                mongoClient.close();

                break;


            //remove playlist from storage
            case "rm":
            case "remove":
            case "delete":
                System.out.println("delete triggered!");
                break;
        }


    }

    @Override
    public String getHelp() {
        return "Lets you safe a playlist in soundys storage for listening later" +
                "Usage: " + Constants.PREFIX + getInvoke() + " [name]";
    }

    @Override
    public String getInvoke() {
        return "pl";
    }
}