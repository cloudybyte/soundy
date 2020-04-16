package net.cloudybyte.bot;

import io.sentry.Sentry;
import net.cloudybyte.bot.core.Constants;
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
                .setActivity(Activity.listening(Constants.PREFIX + "help"));


        builder.addEventListeners(new Listener());


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
