package gq.not11.bot.core.command;

import com.sun.jdi.InvalidTypeException;
import gq.not11.bot.Bot;
import net.dv8tion.jda.core.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler extends ListenerAdapter {

    private Bot bot;
    private Map<String, Command> commands;

    public CommandHandler(Bot bot) {
        this.bot = bot;
        commands = new HashMap<>();
    }

    public void register(Command command) {
        for (String alias : command.getAliases()) {
            commands.put(alias, command);
        }
    }

    public void onGenericGuildMessage(GenericGuildMessageEvent genericGuildMessageEvent) {
        try {
            CommandEvent event = new CommandEvent(genericGuildMessageEvent);

            if (!commands.containsKey(event.getCommand()) || ((GuildMessageReceivedEvent) genericGuildMessageEvent).getAuthor().isBot() || ((GuildMessageReceivedEvent) genericGuildMessageEvent).getAuthor().isFake()) {
                return;
            }

            commands.get(event.getCommand()).run(event);
        } catch (InvalidTypeException e) {
            e.printStackTrace();
        }
    }
}
