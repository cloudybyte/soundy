package net.cloudybyte.bot.listeners;

import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ClickableMessage extends ListenerAdapter {
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getMember().equals(event.getMember().getJDA().getSelfUser())) {
            return;
        }

    }
}

