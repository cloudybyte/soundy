package gq.not11.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import gq.not11.bot.core.audio.TrackScheduler;
import gq.not11.bot.core.command.ICommand;
import gq.not11.bot.util.Colors;
import gq.not11.bot.util.EmbedBuilder;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

import java.util.List;

public class LoopCommand implements ICommand {


    private boolean trackLoop;

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







        
        //TODO THIS ENABLES A GLOBAL LOOP ACROSS ALL GUILDS! MUST BE LOCKED TO ONE GUILD!


       boolean trackLoop;

        if (trackLoop = false) {
            String trackToLoop = player.getPlayingTrack().getInfo().uri;
            trackLoop = true;
            embedBuilder.info(event, "Looping turned on", "Hey! I just stepped on of the loop train! It's ");
        }
        if (trackLoop = true) {
            trackLoop = false;
            embedBuilder.info(event, "Looping turned off", "Hey! I just stepped out of the looping train!");
        }
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getInvoke() {
        return null;
    }

    public boolean isTrackLoop() {
        return trackLoop;
    }

    public void setTrackLoop(boolean trackLoop) {
        this.trackLoop = trackLoop;
    }
}
