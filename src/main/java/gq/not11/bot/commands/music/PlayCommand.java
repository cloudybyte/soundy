package gq.not11.bot.commands.music;


import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import gq.not11.bot.core.Constants;
import gq.not11.bot.core.audio.AudioPlayerSendHandler;
import gq.not11.bot.core.audio.PlayerManager;
import gq.not11.bot.core.audio.TrackScheduler;
import gq.not11.bot.core.command.ICommand;
import gq.not11.bot.util.Colors;
import gq.not11.bot.util.EmbedBuilder;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.core.managers.AudioManager;
//import org.slf4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.sentry.Sentry;
import gq.not11.bot.util.Colors.*;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.sedmelluq.discord.lavaplayer.tools.FriendlyException.Severity.COMMON;


public class PlayCommand implements ICommand, AudioEventListener  {

    private Logger logger = LoggerFactory.getLogger(PlayCommand.class);

    private final YouTube youTube;

    public PlayCommand() {
        YouTube temp = null;

        try {
            temp = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null
            )
                    .setApplicationName("nightplay Youtube Search Engine")
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


        VoiceChannel vc = event.getMember().getVoiceState().getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();


        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioPlayer player = playerManager.createPlayer();
        TrackScheduler trackScheduler = new TrackScheduler(player);


        String BLUE = Colors.BLUE;
        String RESET = Colors.RESET;

        if(args.isEmpty()){
            embedBuilder.warn(event,"Arguments error", "Please provide a song name");
        }



        String input = String.join(" ", args);

        if(!isURL(input)){
            String ytSearched = searchYoutube(input);
            input = ytSearched;

            if(ytSearched == null){
                embedBuilder.warn(event, "Search error", "Nothing found");

                return;
            }
        }

        PlayerManager manager = PlayerManager.getInstance();

        try {
            manager.loadAndPlay(event.getChannel(), input);
            logger.info(BLUE + "Queued " + args.get(0) + " in guild " + event.getGuild().getName() + " by " + event.getAuthor() + RESET);
        }
        catch(NullPointerException e){
            event.getChannel().sendMessage("URL cannot be null!").queue();
            sentry.sendException(e);
        }











        manager.getGuildMusicManager(event.getGuild()).player.setVolume(100);




    }


    private boolean isURL(String input) {
        try{
            new URL(input);
            return true;
        } catch (MalformedURLException ignored) {
            return false;
        }
    }

     @Nullable
    private String searchYoutube(String input){

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

            if (!results.isEmpty()){

                String videoID = results.get(0).getId().getVideoId();


                return "https://www.youtube.com/watch?v=" + videoID;


            }
        }
        catch (Exception e){
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




