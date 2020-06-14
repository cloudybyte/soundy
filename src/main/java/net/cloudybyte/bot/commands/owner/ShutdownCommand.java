package net.cloudybyte.bot.commands.owner;


import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.Colors;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ShutdownCommand implements ICommand {


    private EmbedBuilder embedBuilder = new EmbedBuilder();
    private Logger logger = LoggerFactory.getLogger(ShutdownCommand.class);

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        JDA jda = event.getJDA();




        if (event.getAuthor().getIdLong() == Constants.OWNER){
            if (args.isEmpty()) {
                String RESET = Colors.RESET;
                String PURPLE = Colors.PURPLE;
                logger.info(PURPLE + "Shutdown requested by "+ event.getMessage().getAuthor().getName() + RESET);
                event.getJDA().shutdownNow();
            }

        }
        else {embedBuilder.error(event, "Permission error", "You don't have the permission to perform this action!");}

    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getInvoke() {
        return "shutdown";
    }
}
