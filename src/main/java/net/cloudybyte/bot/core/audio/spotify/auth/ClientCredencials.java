package net.cloudybyte.bot.core.audio.spotify.auth;


import com.sun.org.apache.bcel.internal.Const;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import net.cloudybyte.bot.core.Constants;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
public class ClientCredencials {
        private static final String clientId = Constants.spotifyClientID;
        private static final String clientSecret = Constants.spotifyClientSecret;

        private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();
        private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
                .build();

        public static void clientCredentials_Sync() {
            try {
                final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

                // Set access token for further "spotifyApi" object usage
                spotifyApi.setAccessToken(clientCredentials.getAccessToken());

                System.out.println("Expires in: " + clientCredentials.getExpiresIn());
            } catch (IOException | SpotifyWebApiException | ParseException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        public static void clientCredentials_Async() {
            try {
                final CompletableFuture<ClientCredentials> clientCredentialsFuture = clientCredentialsRequest.executeAsync();

                // Thread free to do other tasks...

                // Example Only. Never block in production code.
                final ClientCredentials clientCredentials = clientCredentialsFuture.join();

                // Set access token for further "spotifyApi" object usage
                spotifyApi.setAccessToken(clientCredentials.getAccessToken());

                System.out.println("Expires in: " + clientCredentials.getExpiresIn());
            } catch (CompletionException e) {
                System.out.println("Error: " + e.getCause().getMessage());
            } catch (CancellationException e) {
                System.out.println("Async operation cancelled.");
            }
        }

        public static void main(String[] args) {
            clientCredentials_Sync();
            clientCredentials_Async();
        }
    }

