package net.cloudybyte.bot.commands.music;

import net.cloudybyte.bot.core.audio.GuildMusicManager;
import net.cloudybyte.bot.core.audio.PlayerManager;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.cloudybyte.bot.util.Reactions;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class PauseCommand implements ICommand {





    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if(guildMusicManager.player.isPaused()) {
            embedBuilder.info(event, "Resuming", "Whoops! I can't pause the music because it was already paused, so I resumed it for you!");
            event.getMessage().addReaction(Reactions.PLAY).queue();
            guildMusicManager.player.setPaused(false);

        }
        else if (!guildMusicManager.player.isPaused()) {
            event.getMessage().addReaction(Reactions.PAUSE).queue();
            guildMusicManager.player.setPaused(true);

        }
    }

    @Override
    public String getHelp() {
        return "Let's you pause the music";
    }

    @Override
    public String getInvoke() {
        return "pause";
    }
}

