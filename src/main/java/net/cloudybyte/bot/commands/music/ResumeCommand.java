package net.cloudybyte.bot.commands.music;


import net.cloudybyte.bot.core.audio.GuildMusicManager;
import net.cloudybyte.bot.core.audio.PlayerManager;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.cloudybyte.bot.util.Reactions;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

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
