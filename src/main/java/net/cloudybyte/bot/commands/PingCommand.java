package net.cloudybyte.bot.commands;


import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.command.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.util.List;

public class PingCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        SentryClient sentry = SentryClientFactory.sentryClient();

        long call = System.currentTimeMillis();
        try {
            event.getMessage().getChannel().sendMessage("Calculating ping ...").queue(message -> {
                long back = System.currentTimeMillis();
                message.editMessage(String.format("%sms", back - call)).queue();
            });
        }
        catch (UnsupportedOperationException | InsufficientPermissionException e){
            sentry.sendException(e);
        }
    }

    @Override
    public String getHelp() {
        return "Shows you the current ping of the bot\n" +
                "Note that this is not the package loss!\n" +
                "Usage: `" + Constants.PREFIX + getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "ping";
    }


}
