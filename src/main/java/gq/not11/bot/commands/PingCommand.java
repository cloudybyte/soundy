package gq.not11.bot.commands;


import gq.not11.bot.core.Constants;
import gq.not11.bot.core.command.ICommand;
import io.sentry.Sentry;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateFeaturesEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.GuildUnavailableException;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;

import javax.print.DocFlavor;
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
        catch (UnsupportedOperationException | GuildUnavailableException | InsufficientPermissionException e){
            sentry.sendException(e);
        }
    }

    @Override
    public String getHelp() {
        return "Shows you the current ping of the bot\n" +
                "Usage: `" + Constants.PREFIX + getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "ping";
    }


}
