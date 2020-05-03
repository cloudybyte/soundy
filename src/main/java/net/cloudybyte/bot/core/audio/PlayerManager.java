package net.cloudybyte.bot.core.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static net.cloudybyte.bot.util.Colors.GREEN;
import static net.cloudybyte.bot.util.Colors.RESET;


public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;
    private static final Logger log = LoggerFactory.getLogger(PlayerManager.class);

    private PlayerManager() {
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }


    public synchronized GuildMusicManager getGuildMusicManager (Guild guild){
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            long guildid = guild.getIdLong();
            musicManager = new GuildMusicManager(playerManager, guildid);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }

    public void loadAndPlay(GuildMessageReceivedEvent event, String trackURL) {
        GuildMusicManager musicManager = getGuildMusicManager(event.getChannel().getGuild());
        TextChannel channel = event.getChannel();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        SentryClient sentry = SentryClientFactory.sentryClient();

        playerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                embedBuilder.queuedCommand(event, track.getInfo().title);
                log.info(GREEN + "Track was loaded! Now playing: " + trackURL + " Title: " + track.getInfo().title + RESET);
                play(musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                PlayerManager playerManager = PlayerManager.getInstance();
                GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
                TrackScheduler scheduler = musicManager.scheduler;
                AudioPlayer player = musicManager.player;
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }
                musicManager.scheduler.getQueue().addAll(playlist.getTracks());
                channel.sendMessage("Adding to queue: " + firstTrack.getInfo().title + " (first track of playlist ``" + playlist.getName() + "`` )").queue();
                scheduler.getQueue().addAll(playlist.getTracks());

                play(musicManager, scheduler.getQueue().poll());
            }

            @Override
            public void noMatches() {
                embedBuilder.error(event, "search error","Nothing found by " + trackURL);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                embedBuilder.error(event, null, "Could not play: " + exception.getMessage());
                log.error("Loading track failed: " + exception.getMessage());
                sentry.sendException(exception);

            }
        });
    }

    private void play(GuildMusicManager musicManager, AudioTrack track){
        musicManager.scheduler.queue(track);
    }

    public static synchronized PlayerManager getInstance() {


        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }

}
