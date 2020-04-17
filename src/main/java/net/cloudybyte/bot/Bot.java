package net.cloudybyte.bot;

import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import io.sentry.Sentry;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.listeners.GuildJoinListener;
import net.cloudybyte.bot.listeners.ReadyListener;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.IOException;


public class Bot {


    long startupTime = System.currentTimeMillis();
    private Listener Listener;
    private ShardManager shardManager;


    private Bot(String[] args) {
        //INITIALIZE SENTRY
        Sentry.init(System.getenv("SENTRY_DSN"));


        DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder()
                .setToken(System.getenv("DISCORD_TOKEN"))
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.listening(Constants.PREFIX + "help"))
                .setAudioSendFactory(new NativeAudioSendFactory());


        builder.addEventListeners(new Listener());
        builder.addEventListeners(new ReadyListener());
        builder.addEventListeners(new GuildJoinListener());
        builder.addEventListeners();


        try {
            shardManager = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) throws IOException {
        new Bot(args);
    }
}
