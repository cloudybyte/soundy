package gq.not11.bot;

import gq.not11.bot.commands.PingCommand;
import gq.not11.bot.commands.music.DisconnectCommand;
import gq.not11.bot.commands.music.JoinCommand;
import gq.not11.bot.commands.music.PlayCommand;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import java.util.concurrent.TimeUnit;
import javax.security.auth.login.LoginException;
import io.sentry.Sentry;






public class Bot {

    private Listener Listener;
    long startupTime = System.currentTimeMillis();

    private ShardManager shardManager;



    private Bot(String[] args) {
        //INITIALIZE SENTRY
        Sentry.init(System.getenv("SENTRY_DSN"));


        DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder()
                .setToken(System.getenv("DISCORD_TOKEN"))
                .setStatus(OnlineStatus.ONLINE)
                .setGame(Game.listening(".help"));



builder.addEventListeners(new Listener());




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
