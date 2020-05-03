package net.cloudybyte.bot.listeners;


import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.cloudybyte.bot.util.Colors;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.cloudybyte.bot.util.EmbedUtil;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GuildJoinListener extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(GuildJoinListener.class);
    SentryClient sentry = SentryClientFactory.sentryClient();
    EmbedBuilder embedBuilder = new EmbedBuilder();
    EmbedUtil embedUtil = new EmbedUtil();
    private String GREEN = Colors.GREEN;
    private String RESET = Colors.RESET;


    //TODO: WILL CAUSE AN EXCEPTION WHEN BOT JOINS A GUILD WHILE OFFLINE

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        TextChannel textChannel = event.getGuild().getTextChannels().stream().filter(TextChannel::canTalk).filter(channel -> channel.getName().toLowerCase().contains("bot") || channel.getName().toLowerCase().contains("command") || channel.getName().toLowerCase().contains("music")).findFirst().orElse(event.getGuild().getTextChannels().stream().filter(TextChannel::canTalk).findFirst().orElse(null));
        if (textChannel != null) {
            textChannel.sendMessage(embedUtil.welcome(event, event.getGuild())).queue();
        }

    }
}

