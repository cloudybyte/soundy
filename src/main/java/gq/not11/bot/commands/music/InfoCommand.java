package gq.not11.bot.commands.music;

import gq.not11.bot.core.Constants;
import gq.not11.bot.core.command.ICommand;
import gq.not11.bot.util.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class InfoCommand implements ICommand {

    private EmbedBuilder embedBuilder = new EmbedBuilder();



    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
            embedBuilder.InfoCommand(event, event.getGuild());
    }

    @Override
    public String getHelp() {
        return "Shows current bot info";
    }

    @Override
    public String getInvoke() {
        return "info";
    }
}
