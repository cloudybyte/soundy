package net.cloudybyte.bot.commands.premium;

import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.core.data.JedisInstanceHolder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Set;

public class VoteCheck implements ICommand {
    Jedis jedis = new Jedis("raspberrypi", 6380);

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        JedisPool pool = JedisInstanceHolder.getInstance();
        try (Jedis jedis = pool.getResource()) {
            jedis.select(0);
            /// ... do stuff here ... for example
            jedis.set("foo", "bar");
            String foobar = jedis.get("foo");
            jedis.zadd("sose", 0, "car"); jedis.zadd("sose", 0, "bike");
            Set<String> sose = jedis.zrange("sose", 0, -1);
        }
        net.cloudybyte.bot.core.premiumcheck.VoteCheck voteCheck = new net.cloudybyte.bot.core.premiumcheck.VoteCheck();
        //String value = play jedis;

       // event.getChannel().sendMessage(value).queue();
        if (voteCheck.getVoted(event.getAuthor().getId())){
            event.getChannel().sendMessage("Good boi!").queue();
        }else {
            event.getChannel().sendMessage("Bad boi!").queue();
        }
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getInvoke() {
        return "votecheck";
    }
}
