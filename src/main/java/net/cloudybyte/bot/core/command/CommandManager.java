package net.cloudybyte.bot.core.command;


import net.cloudybyte.bot.WelcomeCommand;
import net.cloudybyte.bot.commands.HelpCommand;
import net.cloudybyte.bot.commands.PingCommand;
import net.cloudybyte.bot.commands.StatsCommand;
import net.cloudybyte.bot.commands.UptimeCommand;
import net.cloudybyte.bot.commands.music.Playlist.SavePlaylist;
import net.cloudybyte.bot.commands.owner.PremiumCheckCommand;
import net.cloudybyte.bot.commands.owner.ShardCommand;
import net.cloudybyte.bot.commands.owner.ShutdownCommand;
import net.cloudybyte.bot.commands.music.*;
import net.cloudybyte.bot.commands.owner.addtocustomerdb;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
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
        addCommand(new ShutdownCommand());
        addCommand(new VolumeCommand());
        addCommand(new LoopCommand());
        addCommand(new SkipCommand());
        addCommand(new NowPlayingCommand());
        addCommand(new WelcomeCommand());
        addCommand(new QueueListCommand());
        addCommand(new FastForwardCommand());
        addCommand(new BassBoostCommand());
        addCommand(new PremiumCheckCommand());
        addCommand(new addtocustomerdb());
        addCommand(new ShuffleCommand());
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


    public void handleCommand(GuildMessageReceivedEvent event, String prefix) {
        final String[] split = event.getMessage().getContentRaw().replaceFirst(
                "(?i)" + Pattern.quote(prefix), "").split("\\s+");
        final String invoke = split[0].toLowerCase();


        if (commands.containsKey(invoke)) {
            final List<String> args = Arrays.asList(split).subList(1, split.length);

            commands.get(invoke).handle(args, event);
        }
    }
}
