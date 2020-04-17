package net.cloudybyte.bot;


import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.command.CommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.cloudybyte.bot.util.Colors.GREEN;
import static net.cloudybyte.bot.util.Colors.RESET;

public class Listener extends ListenerAdapter {

    private final CommandManager manager = new CommandManager();
    private final Logger logger = LoggerFactory.getLogger(Listener.class);



    @Override
    public void onMessageReceived(MessageReceivedEvent event) {



        User author = event.getAuthor();
        Message message = event.getMessage();
        String content = message.getContentDisplay();

        if (event.isFromType(ChannelType.TEXT)) {

            Guild guild = event.getGuild();
            TextChannel textChannel = event.getTextChannel();

            //logger.info(String.format("(%s) [%s] <%#s>: %s", guild.getName(), textChannel.getName(), author, content));
        }
        else if (event.isFromType(ChannelType.PRIVATE)) {
            //logger.info(String.format("[PRIV] <%#s>: %s", author, content));
        }

    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        if (event.getMessage().getContentRaw().equalsIgnoreCase(Constants.PREFIX + "shutdown") &&
                event.getAuthor().getIdLong() == Constants.OWNER) {

            shutdown(event.getJDA());
            return;
        }
        if (!event.getAuthor().isBot() && !event.getMessage().isWebhookMessage() && event.getMessage().getContentRaw().startsWith(Constants.PREFIX)) {
                    manager.handleCommand(event);
        }
    }

    private void shutdown(JDA jda) {
        jda.shutdown();
    }
}
