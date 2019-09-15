package gq.not11.bot.commands.management;

import gq.not11.bot.core.Constants;
import gq.not11.bot.core.command.ICommand;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.bot.utils.cache.ShardCacheView;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class ShardCommand implements ICommand {
    SentryClient sentry = SentryClientFactory.sentryClient();

    ShardManager shardManager;


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if(event.getMessage().getAuthor().getIdLong() == Constants.OWNER){
            if (args.get(1) == "restart") {

                if (args.get(2) == "all") {
                    shardManager.restart();
                }
                else{try{
                    shardManager.restart(Integer.parseInt(args.get(2)));
                }catch (IllegalArgumentException e){
                sentry.sendException(e);
                event.getChannel().sendMessage("You have to provide a valid shard ID!").queue();
                }
                }

            }





        }

    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getInvoke() {
        return null;
    }
}
