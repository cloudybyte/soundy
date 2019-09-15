package gq.not11.bot.commands.music;

import gq.not11.bot.core.audio.GuildMusicManager;
import gq.not11.bot.core.audio.PlayerManager;
import gq.not11.bot.core.command.ICommand;
import gq.not11.bot.util.EmbedBuilder;

import gq.not11.bot.util.Reactions;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class ResumeCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if(guildMusicManager.player.isPaused()) {
            guildMusicManager.player.setPaused(false);
            event.getMessage().addReaction(Reactions.PLAY).queue();
        }
        else if (!guildMusicManager.player.isPaused()) {
            embedBuilder.info(event, "Pausing", "Whoops! I can't resume the music because it was already playing, so I paused it for you!");
            guildMusicManager.player.setPaused(true);
            event.getMessage().addReaction(Reactions.PAUSE).queue();
        }

    }

    @Override
    public String getHelp() {
        return "Resumes the music for you";
    }

    @Override
    public String getInvoke() {
        return "resume";
    }
}
