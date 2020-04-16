package net.cloudybyte.bot.commands;


import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

public class UptimeCommand implements ICommand {

    private EmbedBuilder embedBuilder = new EmbedBuilder();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {


        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

        long uptime = runtimeMXBean.getUptime();
        long uptimeInSeconds = uptime / 1000;
        long numberOfHours = uptimeInSeconds / (60 * 60);
        long numberOfMinutes = (uptimeInSeconds / 60) - (numberOfHours * 60);
        long numberOfSeconds  = uptimeInSeconds % 60;
        long numberOfDays = numberOfHours / 24;

        String uptimeTotal = String.format("`%s days, %s hours, %s minutes, %s seconds`", numberOfDays, numberOfHours, numberOfMinutes, numberOfSeconds);

        embedBuilder.info(event, "Uptime", uptimeTotal);

    }

    @Override
    public String getHelp() {
        return "Shows you the uptime of nightplay\n" +
                "Usage: `" + Constants.PREFIX + getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "up";
    }
}
