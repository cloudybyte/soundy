/*
 * Copyright (c) Ole Donnermeyer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ole Donnermeyer <ole.donnermeyer@gmx.net>, 2020
 */

package net.cloudybyte.bot.commands.music;


import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.mongodb.*;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.wrapper.spotify.exceptions.detailed.BadRequestException;
import com.wrapper.spotify.exceptions.detailed.NotFoundException;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.audio.PlayerManager;
import net.cloudybyte.bot.core.audio.TrackScheduler;
import net.cloudybyte.bot.core.audio.spotify.SpotifyAPIHandler;
import net.cloudybyte.bot.core.audio.spotify.PlaylistInfo;
import net.cloudybyte.bot.core.audio.spotify.TrackInfo;
import net.cloudybyte.bot.core.audio.youtube.SearchYoutube;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.Colors;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.cloudybyte.bot.util.EmbedUtil;
import net.cloudybyte.bot.util.GuildTrackScheduleHandler;
import net.dv8tion.jda.api.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;


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
        EmbedUtil embedUtil = new EmbedUtil();


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
        SpotifyAPIHandler SpotifyAPIHandler = new SpotifyAPIHandler();


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
        System.out.println(Constants.DBUri);
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


            PlayerManager manager = PlayerManager.getInstance();


            if (input.startsWith("https://open.spotify.com/track/")) {
                try {
                    TrackInfo trackInfo = SpotifyAPIHandler.getTrackInfo(input);
                    String ytsearched = SearchYoutube.searchyt(trackInfo.getName(trackInfo) + " " + trackInfo.getArtist(trackInfo));
                    input = ytsearched;
                } catch (NullPointerException e) {
                    embedBuilder.error(event, "Link Error", "That link is not a valid spotify track link!");
                    return;
                }


            } else if (input.startsWith("https://open.spotify.com/playlist/")) {
                try {
                    LinkedList<TrackInfo> trackMap = SpotifyAPIHandler.getPlaylistMap(input);

                PlaylistInfo playlistInfo = SpotifyAPIHandler.getPlaylistInfo(input);
                //queue all songs (except the first one) async
                TrackInfo trackInfo1st = trackMap.getFirst();

                event.getChannel().sendMessage(embedUtil.SpotifyPlaylistQueued(event, playlistInfo)).queue();


                String ytsearched = SearchYoutube.searchyt(trackInfo1st.getName(trackInfo1st) + " " + trackInfo1st.getArtist(trackInfo1st));
                trackMap.remove(trackMap.getFirst());
                input = ytsearched;
                CompletableFuture.runAsync(() -> {
                    Random random = new Random();
                    for (TrackInfo trackInfo : trackMap) {
                        String YTsearched = SearchYoutube.searchyt(trackInfo.getName(trackInfo) + " " + trackInfo.getArtist(trackInfo));
                        try {
                            Thread.sleep(random.nextInt(2500));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        manager.loadSilent(event, YTsearched);
                    }
                });

                } catch (NullPointerException e) {
                    embedBuilder.error(event, "Link Error", "That link is not a valid spotify playlist link!");
                    return;
                }

            }


            //search YT
            if (!isURL(input)) {
                String ytSearched = SearchYoutube.searchyt(input);
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


            //Set volume

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
                String ytSearched = SearchYoutube.searchyt(input);
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


    //Not used anymore -> Scraper
 /*   @Nullable
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
    }*/


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