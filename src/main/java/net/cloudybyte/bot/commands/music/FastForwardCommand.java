package net.cloudybyte.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.audio.GuildMusicManager;
import net.cloudybyte.bot.core.audio.PlayerManager;
import net.cloudybyte.bot.core.audio.TrackScheduler;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.cloudybyte.bot.util.Reactions;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class FastForwardCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;
        AudioPlayer player = musicManager.player;
        EmbedBuilder embedBuilder = new EmbedBuilder();
        long currentPosition = player.getPlayingTrack().getPosition();


        if (player.getPlayingTrack() == null) {
            embedBuilder.error(event, "nothing is playing", "\\*silence\\*");
            return;
        }
        if (args.isEmpty()) {

            long skipTo = currentPosition + 10000;
            player.getPlayingTrack().setPosition(skipTo);
            event.getMessage().addReaction(Reactions.FAST_FORWARD).queue();
            return;
        }

        if (currentPosition + (Long.parseLong(args.get(0)) * 1000) >= player.getPlayingTrack().getDuration()) {
            scheduler.nextTrack();
            try {
                embedBuilder.nowPlaying(event, musicManager.player.getPlayingTrack().getInfo().title);
            } catch (NullPointerException ignored) {
            }
        }


        try {
            long skipTo = currentPosition + (Long.parseLong(args.get(0)) * 1000);
            player.getPlayingTrack().setPosition(skipTo);
            event.getMessage().addReaction(Reactions.FAST_FORWARD).queue();
        } catch (NumberFormatException ignored) {
            embedBuilder.error(event, "not a number", "you have to provide a valid number in order to skip");
        }
    }

    @Override
    public String getHelp() {
        return "Lets you fast forward to a specific position in the track\n" +
                "Usage: " + Constants.PREFIX + "ff";
    }

    @Override
    public String getInvoke() {
        return "ff";
    }
}
