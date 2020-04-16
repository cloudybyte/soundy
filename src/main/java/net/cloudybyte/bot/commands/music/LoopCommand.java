package net.cloudybyte.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import net.cloudybyte.bot.core.audio.TrackScheduler;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.Colors;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

public class LoopCommand implements ICommand {



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

        boolean trackLoop = trackScheduler.isTrackLooped();


        if (trackLoop = false) {

            embedBuilder.info(event, "Looping turned on", "Hey! I just stepped on of the loop train!");
        }
        if (trackLoop = true) {
            trackLoop = false;
            embedBuilder.info(event, "Looping turned off", "Hey! I just fell of the looping train!");
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

}
