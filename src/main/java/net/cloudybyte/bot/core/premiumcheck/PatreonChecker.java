package net.cloudybyte.bot.core.premiumcheck;

import com.github.jasminb.jsonapi.JSONAPIDocument;
import com.patreon.PatreonAPI;
import com.patreon.PatreonOAuth;
import com.patreon.PatreonOAuth;
import com.patreon.resources.User;
import com.patreon.resources.Pledge;
import com.sun.tools.javac.util.Log;

import java.io.IOException;
import java.util.List;

public class PatreonChecker {


    String clientId = "_HHW94dA-wnhQxfQHb2ryIQe_MoEX7MdD-0j6Ah3Wg7K33bWP3UeiDTksrFU80mv"; // Get this when you set up your client
    String clientSecret = "KFuW8YSL29TpHG7nU4-Hey68UiFr-4RLSiRnwZz5x5Dxmb6LwK_LUIom7h2RaCT-"; // Get this when you set up your client
    String redirectUri = "https://invite.soundy.gq"; // Provide this to set up your client

    String code = null; // Get this from the query parameter `code`

    PatreonOAuth oauthClient = new PatreonOAuth(clientId, clientSecret, redirectUri);
    PatreonOAuth.TokensResponse tokens;
    //Store the refresh TokensResponse in your data store
    String accessToken;

    {
        assert tokens != null;
        accessToken = tokens.getAccessToken();
        try {
            tokens = oauthClient.getTokens(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializePatreons() {
        PatreonAPI apiClient = new PatreonAPI(accessToken);
        JSONAPIDocument<User> userResponse = null;
        try {
            userResponse = apiClient.fetchUser();
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user = userResponse.get();
        System.out.println(user.getFullName());
        List<Pledge> pledges = user.getPledges();
        if (pledges != null && pledges.size() > 0) {
            Pledge pledge = pledges.get(0);
            System.out.println((pledge.getAmountCents()));
        }

        System.out.println("Initialized Patreons!");
// You should save the user's PatreonOAuth.TokensResponse in your database
// (for refreshing their Patreon data whenever you like),
// along with any relevant user info or pledge info you want to store.
    }
}