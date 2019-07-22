package gq.not11.bot.core.command;

public abstract class Command {

    private String description;
    private String[] aliases;
    private String usage;

    public Command(String description, String[] aliases, String usage) {
        this.description = description;
        this.aliases = aliases;
        this.usage = usage;
    }

    public abstract void run(CommandEvent event);

    public String getDescription() {
        return description;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getUsage() {
        return usage;
    }
}
