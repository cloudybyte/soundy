package gq.not11.bot.core.command;

import com.sun.jdi.InvalidTypeException;
import net.dv8tion.jda.core.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;

public class CommandEvent {

    private CommandType type;
    private String prefix;
    private String command;
    private String content;
    private GenericGuildMessageEvent raw;

    public CommandEvent(GenericGuildMessageEvent event) throws InvalidTypeException {
        if (event instanceof GuildMessageReceivedEvent) {
            type = CommandType.NEW;
            raw = event;

            prefix = System.getenv("PREFIX");
            command = ((GuildMessageReceivedEvent) event).getMessage().getContentDisplay().substring(prefix.length()).split(" ")[0];

            content = ((GuildMessageReceivedEvent) event).getMessage().getContentDisplay().substring(prefix.length() + command.length());

            if (content.startsWith(" ")) {
                content = content.substring(1);
            }
        } else if (event instanceof GuildMessageUpdateEvent) {
            type = CommandType.EDIT;
            raw = event;
            // IMPLEMENT LATER ON
        } else {
            throw new InvalidTypeException("CommandEvent cannot be initialized with that GuildMessageEvent!");
        }
    }

    public CommandType getType() {
        return type;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getCommand() {
        return command;
    }

    public String getContent() {
        return content;
    }

    public GenericGuildMessageEvent getRaw() {
        return raw;
    }
}
