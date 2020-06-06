/*
 * Copyright (c) Ole Donnermeyer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ole Donnermeyer <ole.donnermeyer@gmx.net>, 2020
 */

package net.cloudybyte.bot.commands.music;


import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.mongodb.*;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.audio.PlayerManager;
import net.cloudybyte.bot.core.audio.TrackScheduler;
import net.cloudybyte.bot.core.audio.youtube.SearchYoutube;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.Colors;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.cloudybyte.bot.util.GuildTrackScheduleHandler;
import net.dv8tion.jda.api.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.NoSuchElementException;


public class PlayCommand implements ICommand, AudioEventListener {

    private final YouTube youTube;
    private Logger logger = LoggerFactory.getLogger(PlayCommand.class);

    public PlayCommand() {
        YouTube temp = null;

        try {
            temp = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null
            )
                    .setApplicationName("soundy Youtube Search Engine")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        youTube = temp;
    }


    public void onEvent(AudioEvent event) {

    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        SentryClient sentry = SentryClientFactory.sentryClient();
        EmbedBuilder embedBuilder = new EmbedBuilder();


     /*   PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        AudioPlayer player = musicManager.player;
        TrackScheduler scheduler = musicManager.scheduler;
        if (player.isPaused()){
            player.setPaused(false);
            return;
        } */

        VoiceChannel vc = event.getMember().getVoiceState().getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();

        long guildid = event.getGuild().getIdLong();

        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioPlayer player = playerManager.createPlayer();
        TrackScheduler trackScheduler = new TrackScheduler(player, guildid);
        GuildTrackScheduleHandler.addTrackScheduler(event.getGuild(), trackScheduler);
        JoinCommand joinCommand = new JoinCommand();


        String BLUE = Colors.BLUE;
        String RESET = Colors.RESET;


        //Check for args
        if (args.isEmpty()) {
            embedBuilder.warn(event, "Arguments error", "Please provide a song name");
            return;
        }

        //Connect to DB
        MongoClient mongoClient = null;
        mongoClient = new MongoClient(new MongoClientURI(Constants.DBUri));
        DB database = mongoClient.getDB("soundy");
        DBCollection collection = database.getCollection("volumes");


        //search for guild in db
        DBObject query = new BasicDBObject("guildid", guildid);
        BasicDBObject select = new BasicDBObject();
        select.put("volume", 1);
        DBCursor cursor = collection.find(query, select);
        try {
            BasicDBObject obj = (BasicDBObject) cursor.next();


            //Connect to VC if not already connected
            if (audioManager.getConnectionStatus().equals(ConnectionStatus.NOT_CONNECTED)) {
                joinCommand.joinVC(event.getMember(), event.getGuild(), event);
            }


            String input = String.join(" ", args);

            //search YT
            if (!isURL(input)) {
                String ytSearched = SearchYoutube.searchyt(input);
                input = ytSearched;

                if (ytSearched == null) {
                    embedBuilder.warn(event, "Search error", "Nothing found");

                    return;
                }
            }

            PlayerManager manager = PlayerManager.getInstance();

            //Connect to VC (again)
            if (audioManager.getConnectedChannel() == null) {
                audioManager.openAudioConnection(event.getMember().getVoiceState().getChannel());
            }

            //Play track
            manager.loadAndPlay(event, input);
            logger.info(BLUE + "Queued " + args.get(0) + " in guild " + event.getGuild().getName() + " by " + event.getAuthor() + RESET);

            //Set volume


            System.out.println("obj.getString(\"volume\") = " + obj.getString("volume"));
            Integer volume = Integer.parseInt(obj.getString("volume"));
            manager.getGuildMusicManager(event.getGuild()).player.setVolume(volume);


        } catch (NoSuchElementException e) {
            PlayerManager manager = PlayerManager.getInstance();
            collection.update(new BasicDBObject("guildid", guildid), new BasicDBObject("$set", new BasicDBObject("volume", 100)), true, false);
            //Connect to VC if not already connected
            if (audioManager.getConnectionStatus().equals(ConnectionStatus.NOT_CONNECTED)) {
                joinCommand.joinVC(event.getMember(), event.getGuild(), event);
            }

            String input = String.join(" ", args);

            //search YT
            if (!isURL(input)) {
                String ytSearched = searchYoutube(input);
                input = ytSearched;

                if (ytSearched == null) {
                    embedBuilder.warn(event, "Search error", "Nothing found");

                    return;
                }
            }


            //Connect to VC (again)
            if (audioManager.getConnectedChannel() == null) {
                audioManager.openAudioConnection(event.getMember().getVoiceState().getChannel());
            }

            //Play track
            manager.loadAndPlay(event, input);
            logger.info(BLUE + "Queued " + args.get(0) + " in guild " + event.getGuild().getName() + " by " + event.getAuthor() + RESET);
            manager.getGuildMusicManager(event.getGuild()).player.setVolume(100);
        }
    }


    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isURL(String input) {
        try {
            new URL(input);
            return true;
        } catch (MalformedURLException ignored) {
            return false;
        }
    }

    @Nullable
    private String searchYoutube(String input) {


        try {
            List<SearchResult> results = youTube.search()
                    .list("id,snippet")
                    .setQ(input)
                    .setMaxResults(1L)
                    .setType("video")
                    .setFields("items(id/kind,id/videoId,snippet/title)")
                    .setKey(System.getenv("YOUTUBE_KEY"))
                    .execute()
                    .getItems();

            if (!results.isEmpty()) {

                String videoID = results.get(0).getId().getVideoId();


                return "https://www.youtube.com/watch?v=" + videoID;

            }
        } catch (GoogleJsonResponseException ignore) {

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public String getHelp() {
        return "Plays a song\n" +
                "Usage: `" + Constants.PREFIX + getInvoke() + " [song url]`";
    }

    @Override
    public String getInvoke() {
        return "play";
    }
}