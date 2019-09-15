package gq.not11.bot.commands.music;

import gq.not11.bot.core.audio.GuildMusicManager;
import gq.not11.bot.core.audio.PlayerManager;
import gq.not11.bot.core.command.ICommand;
import gq.not11.bot.util.EmbedBuilder;

import gq.not11.bot.util.Reactions;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class StopCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
        EmbedBuilder embedBuilder = new EmbedBuilder();


        guildMusicManager.scheduler.getQueue().clear();
        guildMusicManager.player.stopTrack();
        guildMusicManager.player.setPaused(false);

        embedBuilder.success(event, "Stopping the music!", "Stopping the music and clearing the queue!");
        event.getMessage().addReaction(Reactions.STOP).queue();



    }

    @Override
    public String getHelp() {
        return "Stops the music player";
    }

    @Override
    public String getInvoke() {
        return "stop";
    }
}
