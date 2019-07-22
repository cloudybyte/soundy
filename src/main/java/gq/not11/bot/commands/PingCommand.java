package gq.not11.bot.commands;

import gq.not11.bot.core.command.Command;
import gq.not11.bot.core.command.CommandEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class PingCommand extends Command {

    public PingCommand() {
        super("Let's you calculate the bot's current ping", new String[]{"ping"}, "");
    }

    @Override
    public void run(CommandEvent event) {
        GuildMessageReceivedEvent raw = ((GuildMessageReceivedEvent) event.getRaw());

        long call = System.currentTimeMillis();
        raw.getChannel().sendMessage("Calculating ping ...").queue(message -> {
            long back = System.currentTimeMillis();
            message.editMessage(String.format("%sms", back - call)).queue();
        });
    }
}
