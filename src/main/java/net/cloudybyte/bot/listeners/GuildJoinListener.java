package net.cloudybyte.bot.listeners;


import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.data.MySQLManager;
import net.cloudybyte.bot.util.Colors;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;


public class GuildJoinListener extends ListenerAdapter {
    private String GREEN = Colors.GREEN;
    private String RESET = Colors.RESET;
    SentryClient sentry = SentryClientFactory.sentryClient();
    private final Logger logger = LoggerFactory.getLogger(GuildJoinListener.class);
    MySQLManager mySQLManager = new MySQLManager(Constants.DBHost, Constants.DBport, Constants.DBUser , Constants.DBpw, Constants.DBName);



    //TODO: WILL CAUSE AN EXCEPTION WHEN BOT JOINS A GUILD WHILE OFFLINE

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        long guildid = event.getGuild().getIdLong();
        try {
            mySQLManager.connect();
            mySQLManager.insertandupdate("volumes", new String[]{"guildid", "volume"},new Object[]{guildid, 100}, "volume", 100);
            mySQLManager.insert("tracklooping", new String[]{"guildid", "looped"}, new Object[]{guildid, 0});
        } catch (MySQLManager.MySQL_NotConnectedQueryException e) {
            e.printStackTrace();
        }
    }
    }

