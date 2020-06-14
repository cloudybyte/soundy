package net.cloudybyte.bot;

import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import io.sentry.Sentry;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.audio.spotify.SpotifyAPIHandler;
import net.cloudybyte.bot.core.botlists.BotsForDiscord;
import net.cloudybyte.bot.core.botlists.PostStats;
import net.cloudybyte.bot.core.data.cache.AlwaysOnCache;
import net.cloudybyte.bot.core.data.cache.YTKeyCache;
import net.cloudybyte.bot.core.data.cache.YTStatusCache;
import net.cloudybyte.bot.listeners.AutoDisconnect;
import net.cloudybyte.bot.listeners.GuildLeaveListener;
import net.cloudybyte.bot.listeners.ReadyListener;
import net.cloudybyte.bot.util.PrintStartupBanner;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/*
    * @author cloudybyte#9301
    */

public class Bot {


    long startupTime = System.currentTimeMillis();
    private Logger logger = LoggerFactory.getLogger(Bot.class);
    private Listener Listener;
    private ShardManager shardManager;
    BotsForDiscord botsForDiscord = new BotsForDiscord();
    SpotifyAPIHandler SpotifyAPIHandler = new SpotifyAPIHandler();
    AlwaysOnCache alwaysOnCache;
    YTStatusCache ytStatusCache;
    YTKeyCache ytKeyCache;

    private Bot(String[] args) {
        //Display Banner
        PrintStartupBanner.print();

      //  getPlaylistContent.getTrackName("https://open.spotify.com/track/5ZONFzb7kpERR8AClG0Mo7?si=g5FMbuHcTYKcKRJ30vpgCA");



        logger.info("[CORE] Initializing cache");
        //Init caches
        AlwaysOnCache.initCache();
        logger.info("[CORE] Cache initialized!");


        //INITIALIZE SENTRY
        Sentry.init(System.getenv("SENTRY_DSN"));

        logger.info("[CORE] Building shardManager");
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


        logger.info("[CORE] shardManager built!");

        //Schedule Statistics Poster
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(() -> PostStats.postToTopGG(shardManager), 0, 5, TimeUnit.MINUTES);


    }

    public static void main(String[] args) {
        new Bot(args);
    }
}
