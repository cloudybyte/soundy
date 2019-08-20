package gq.not11.bot.commands;

import gq.not11.bot.core.command.Command;
import gq.not11.bot.core.command.CommandEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class UptimeCommand extends Command {

    public UptimeCommand() {
        super("Shows you the uptime of nexot", new String[]{"uptime", "up"}, "");
    }

    @Override
    public void run(CommandEvent event) {
        GuildMessageReceivedEvent raw = ((GuildMessageReceivedEvent) event.getRaw());

        //TODO: UPTIMECOMMAND

    }
}
