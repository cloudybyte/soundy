package net.cloudybyte.bot.commands;


import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.command.CommandManager;
import net.cloudybyte.bot.core.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class HelpCommand implements ICommand {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (args.isEmpty()) {
            generateAndSendEmbed(event);
            return;
        }

        String joined = String.join("", args);

        ICommand command = manager.getCommand(joined);

        if(command == null) {
            event.getChannel().sendMessage("The command `" + joined + "` does not exist\n" +
                    "Use `" + Constants.PREFIX + getInvoke() + "` for a list of commands").queue();
            return;
        }

        String message = "Command help for `" + command.getInvoke() + "`\n" + command.getHelp();

        event.getChannel().sendMessage(message).queue();



    }






    private void generateAndSendEmbed(GuildMessageReceivedEvent event) {

        EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("A list of all my commands:")
                    .setColor(Color.white);

        StringBuilder descriptionBuilder = builder.getDescriptionBuilder();

        manager.getCommands().forEach(
                (command) -> descriptionBuilder.append('`').append(command.getInvoke()).append("`\n")
                );
        descriptionBuilder.append("If you have problems, please join the support guild: ");
        //TODO: Permission check if the bot can send embed messages
        event.getChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public String getHelp() {
        return "Shows a list of all commands\n" +
                "Usage: `" + Constants.PREFIX + getInvoke() + " [command]`";
    }

    @Override
    public String getInvoke() {
        return "help";
    }
}
