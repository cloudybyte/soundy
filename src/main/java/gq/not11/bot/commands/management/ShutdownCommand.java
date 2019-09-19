package gq.not11.bot.commands.management;

import gq.not11.bot.core.Constants;
import gq.not11.bot.core.command.ICommand;
import gq.not11.bot.util.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gq.not11.bot.util.Colors;

import java.util.List;

public class ShutdownCommand implements ICommand {


    private EmbedBuilder embedBuilder = new EmbedBuilder();
    private Logger logger = LoggerFactory.getLogger(ShutdownCommand.class);

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {




        if (event.getAuthor().getIdLong() == Constants.OWNER){
            if (args.isEmpty()) {
                String RESET = Colors.RESET;
                String PURPLE = Colors.PURPLE;
                logger.info(PURPLE + "Shutdown requested by" + RESET);
                event.getJDA().asBot().getShardManager().shutdown();
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
