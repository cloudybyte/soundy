
package net.cloudybyte.bot.commands.management;

import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.core.data.PrefixCache;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class ChangePrefixCommand implements ICommand {



    EmbedBuilder embedBuilder = new EmbedBuilder();



    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)){
            embedBuilder.error(event, "Permission error", "Whoops! You do not have the permission `Manage Server` which is required to use this command :(");
            return;
        }

        if (args.isEmpty()) {
            embedBuilder.warn(event, "Argument error", "You must provide a prefix in order to change it");
            return;
        }

        final String newPrefix = String.join("", args);
        PrefixCache.PREFIXES.put(event.getGuild().getIdLong(), newPrefix);
        embedBuilder.changePrefixSuccess(event, newPrefix);


    }

    @Override
    public String getHelp() {
        return "Lets you define a custom prefix for this server \n" +
                "Usage: " + Constants.PREFIX + "setprefix [PREFIX HERE]";
    }

    @Override
    public String getInvoke() {
        return "setprefix";
    }
}
