package net.cloudybyte.bot.core.premiumcheck;

import org.discordbots.api.client.DiscordBotListAPI;
import redis.clients.jedis.Jedis;

import java.util.concurrent.atomic.AtomicBoolean;

public class VoteCheck {
    boolean voted = false;
    DiscordBotListAPI api = new DiscordBotListAPI.Builder()
            .token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjcwMDEwMzAyMTUxODEyNzEwNSIsImJvdCI6dHJ1ZSwiaWF0IjoxNTkwNjEyMDY1fQ.-d-RBzv05JpqxvJNO34U5QmopmnHUjojmRtwCRd2HOA")
            .botId("700103021518127105")
            .build();


    public boolean getVoted(String userID) {
        AtomicBoolean voted = new AtomicBoolean(true);







        api.hasVoted(userID).whenComplete((hasVoted, e) -> {
            if (hasVoted)
                voted.set(true);
            else
                voted.set(false);
        });
        return voted.get();
    }
}