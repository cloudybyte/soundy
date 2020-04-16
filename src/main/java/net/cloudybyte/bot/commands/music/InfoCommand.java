package net.cloudybyte.bot.commands.music;


import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

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
