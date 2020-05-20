package net.cloudybyte.bot.commands;

import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class StatusCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.info(event, "Statuspage", "You can visit soundys statuspage by clicking on this link: \n https://status.soundy.gq/ ");
    }

    @Override
    public String getHelp() {
        return "Returns the link to soundys Statuspage";
    }

    @Override
    public String getInvoke() {
        return "status";
    }
}
