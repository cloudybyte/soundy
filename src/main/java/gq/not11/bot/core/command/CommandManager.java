package gq.not11.bot.core.command;

import gq.not11.bot.commands.HelpCommand;
import gq.not11.bot.commands.PingCommand;
import gq.not11.bot.commands.StatsCommand;
import gq.not11.bot.commands.UptimeCommand;
import gq.not11.bot.commands.management.ShardCommand;
import gq.not11.bot.commands.music.*;
import gq.not11.bot.core.Constants;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;


import java.util.*;
import java.util.regex.Pattern;

public class CommandManager {

    private final Map<String, ICommand> commands = new HashMap<>();



    public CommandManager() {

        addCommand(new PingCommand());
        addCommand(new HelpCommand(this));
        addCommand(new UptimeCommand());

        addCommand(new JoinCommand());
        addCommand(new DisconnectCommand());
        addCommand(new PlayCommand());
        addCommand(new PauseCommand());
        addCommand(new ResumeCommand());
        addCommand(new StopCommand());
        addCommand(new InfoCommand());
        addCommand(new StatsCommand());
        addCommand(new ShardCommand());
    }






    private void addCommand(ICommand command) {
        if (!commands.containsKey(command.getInvoke())) {
            commands.put(command.getInvoke(), command);
        }
    }
    public Collection<ICommand> getCommands() {
        return commands.values();
    }

    public ICommand getCommand(@NotNull String name) {
        return commands.get(name);
    }



    public void handleCommand(GuildMessageReceivedEvent event) {
        final String[] split = event.getMessage().getContentRaw().replaceFirst(
                "(?i)" + Pattern.quote(Constants.PREFIX), "").split("\\s+");
        final String invoke = split[0].toLowerCase();




        if(commands.containsKey(invoke)){
            final List<String> args = Arrays.asList(split).subList(1, split.length);

            commands.get(invoke).handle(args, event);
        }

    }
}
