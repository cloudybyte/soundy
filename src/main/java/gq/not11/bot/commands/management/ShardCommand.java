package gq.not11.bot.commands.management;

import gq.not11.bot.core.Constants;
import gq.not11.bot.core.command.ICommand;
import gq.not11.bot.util.EmbedBuilder;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.bot.utils.cache.ShardCacheView;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class ShardCommand implements ICommand {
    SentryClient sentry = SentryClientFactory.sentryClient();

    ShardManager shardManager;
    EmbedBuilder embedBuilder = new EmbedBuilder();


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String args1 = args.get(0);
        String args2 = args.get(2);


        if(event.getMessage().getAuthor().getIdLong() == Constants.OWNER){
            if (args1.equals("restart")) {

                if (args2.equals("all")) {
                    shardManager.restart();
                }
                else{try{
                    shardManager.restart(Integer.parseInt(args2));
                }catch (IllegalArgumentException e){
                sentry.sendException(e);
                event.getChannel().sendMessage("You have to provide a valid shard ID!").queue();
                }
                }

            }

            else if (args1.equals("running")){
                embedBuilder.shardNumberRunning(event);
            }

            else if (args1.equals("ping")){
                embedBuilder.shardAveragePing(event);
            }

            else if (args1.equals("application")){
                embedBuilder.shardApplicationInfo(event);
            }

            else if (args1.equals("shutdown")){
                if(args2.isEmpty()){
                    embedBuilder.error(event, "Argument Error", "You have to provide a shard ID! To shutdown the bot completely please use " + Constants.PREFIX + "shutdown");
                }
                try{
                    shardManager.shutdown(Integer.parseInt(args2));
                }
                catch (IllegalArgumentException e){
                    sentry.sendException(e);
                    embedBuilder.error(event, "Argument Error", "Please provide a number in order to specify a shard!");
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
