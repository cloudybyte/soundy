package net.cloudybyte.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.audio.GuildMusicManager;
import net.cloudybyte.bot.core.audio.PlayerManager;
import net.cloudybyte.bot.core.audio.TrackScheduler;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class NowPlayingCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;
        AudioPlayer player = musicManager.player;
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (player.getPlayingTrack() == null){
            embedBuilder.error(event, "\\*silence\\*", "I do not hear anything, do you?");
            return;
        }
        boolean isStream = player.getPlayingTrack().getInfo().isStream;

        embedBuilder.nowPlayingCommand(event, player.getPlayingTrack().getInfo().title,player.getPlayingTrack().getPosition(), player.getPlayingTrack().getInfo().length, isStream);

    }

    @Override
    public String getHelp() {
        return "returns the playing song\n" +
        "Usage: `" + Constants.PREFIX + "np`";
    }

    @Override
    public String getInvoke() {
        return "np";
    }
}
