package net.cloudybyte.bot.core.botlists;

import net.cloudybyte.bot.Bot;
import net.dv8tion.jda.api.sharding.ShardManager;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PostStats {
    private static Logger logger = LoggerFactory.getLogger(PostStats.class);

    //Post stats to various botlists

        //Post stats to top.gg
        public static void postToTopGG(ShardManager shardManager){
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n    \"server_count\": "  + shardManager.getGuilds().size() +  "\n}");
            Request request = new Request.Builder()
                    .url("https://top.gg/api/bots/700103021518127105/stats")
                    .method("POST", body)
                    .addHeader("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjcwMDEwMzAyMTUxODEyNzEwNSIsImJvdCI6dHJ1ZSwiaWF0IjoxNTkwNjEyMDY1fQ.-d-RBzv05JpqxvJNO34U5QmopmnHUjojmRtwCRd2HOA")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Cookie", "__cfduid=d937df95ecd5908b2ba044bb7993045671590611969")
                    .build();

            try {
                ResponseBody responseBody = client.newCall(request).execute().body();
                responseBody.close();
                logger.info("[BOTLISTS] Posted stats to Botlist! (TopGG)");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }



}
