package gq.not11.bot.commands.music;


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
import gq.not11.bot.core.audio.AudioPlayerSendHandler;
import gq.not11.bot.core.audio.PlayerManager;
import gq.not11.bot.core.audio.TrackScheduler;
import gq.not11.bot.core.command.Command;
import gq.not11.bot.core.command.CommandEvent;
import gq.not11.bot.util.Colors;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.core.managers.AudioManager;
//import org.slf4j.Logger;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import io.sentry.Sentry;
import gq.not11.bot.util.Colors.*;

import static com.sedmelluq.discord.lavaplayer.tools.FriendlyException.Severity.COMMON;


public class PlayCommand extends Command implements AudioEventListener  {

    public PlayCommand() {
        super("Plays a song", new String[]{"play"}, ".play [song url]");
    }


    private static final Logger log = LoggerFactory.getLogger(PlayCommand.class);

    @Override
    public void run(CommandEvent event) {

        GuildMessageReceivedEvent raw = ((GuildMessageReceivedEvent) event.getRaw());

        SentryClient sentry = SentryClientFactory.sentryClient();


        Guild guild = raw.getGuild();
        Member member = raw.getMember();
        VoiceChannel vc = member.getVoiceState().getChannel();
        AudioManager audioManager = guild.getAudioManager();


        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioPlayer player = playerManager.createPlayer();
        TrackScheduler trackScheduler = new TrackScheduler(player);

        TextChannel channel = raw.getChannel();

        String BLUE = Colors.BLUE;
        String RESET = Colors.RESET;










        PlayerManager manager = PlayerManager.getInstance();

        try {
            String[] args = CommandEvent.getArgs;
            System.out.println(BLUE + args[0] + RESET);
            manager.loadAndPlay(raw.getChannel(), args[0]);
        }
        catch(NullPointerException e){
            raw.getChannel().sendMessage("URL cannot be null!").queue();
            sentry.sendException(e);
        }







        manager.getGuildMusicManager(raw.getGuild()).player.setVolume(60);




        }

    @Override
    public void onEvent(AudioEvent event) {

    }
}

