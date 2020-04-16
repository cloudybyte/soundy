package net.cloudybyte.bot.commands;

import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.Colors;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class StatsCommand implements ICommand {

    private EmbedBuilder embedBuilder = new EmbedBuilder();
    private JDA jda;

    String YELLOW = Colors.YELLOW;
    String RESET = Colors.RESET;





    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (event.getMessage().getAuthor().getIdLong() == Constants.OWNER) {embedBuilder.StatsCommand(event, event.getGuild());}
        else {embedBuilder.error(event, "Permission error", "You don't have the permissions to perform this command!");}



    }

    @Override
    public String getHelp() {
        return "Some statistics";
    }

    @Override
    public String getInvoke() {
        return "stats";
    }
}
