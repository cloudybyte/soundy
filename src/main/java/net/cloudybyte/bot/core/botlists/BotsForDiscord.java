package net.cloudybyte.bot.core.botlists;

import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.util.Colors;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;


public class BotsForDiscord {
        public static void sendServerCount(String botid, long servercount) throws Exception {
            String payload = "data={" +
                    "\"server_count\": \"" + servercount +"\"";
            StringEntity entity = new StringEntity(payload,
                    ContentType.APPLICATION_JSON);

            HttpClient httpClient = HttpClientBuilder.create().build();
            //HttpPost request = new HttpPost("https://botsfordiscord.com/api/webhooktest");
            HttpPost request = new HttpPost("https://botsfordiscord.com/api/bot/:700103021518127105");
            request.setEntity(entity);
            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization", Constants.BotsForDiscordToken);

            HttpResponse response = httpClient.execute(request);
            System.out.println(Colors.CYAN + response.getStatusLine().getStatusCode() + Colors.RESET);
        }

}
