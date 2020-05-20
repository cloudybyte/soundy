package net.cloudybyte.bot;

import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import io.sentry.Sentry;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.botlists.BotsForDiscord;
import net.cloudybyte.bot.core.premiumcheck.PremiumTierCache;
import net.cloudybyte.bot.listeners.AutoDisconnect;
import net.cloudybyte.bot.listeners.GuildLeaveListener;
import net.cloudybyte.bot.listeners.ReadyListener;
import net.dv8tion.jda.api.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;


public class Bot {


    long startupTime = System.currentTimeMillis();
    private Listener Listener;
    private ShardManager shardManager;
    BotsForDiscord botsForDiscord = new BotsForDiscord();


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
    //  builder.addEventListeners(new GuildJoinListener());
        builder.addEventListeners(new GuildLeaveListener());
        builder.addEventListeners(new AutoDisconnect());

        try {
            shardManager = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new Bot(args);
    }
}
