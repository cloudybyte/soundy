package net.cloudybyte.bot.core.audio.spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;
import net.cloudybyte.bot.core.Constants;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.LinkedList;

public class GetPlaylistContent {
    // For all requests an access token is needed
    SpotifyApi spotifyApi = new SpotifyApi.Builder()
           // .setAccessToken("taHZ2SdB-bPA3FsK3D7ZN5npZS47cMy-IEySVEGttOhXmqaVAIo0ESvTCLjLBifhHOHOIuhFUKPW1WMDP7w6dj3MAZdWT8CLI2MkZaXbYLTeoDvXesf2eeiLYPBGdx8tIwQJKgV8XdnzH_DONk")
            .setClientId(Constants.spotifyClientID)
            .setClientSecret(Constants.spotifyClientSecret)
            .build();

            LinkedList<String> retunable = new LinkedList<>();


            public LinkedList<String> getTrackName(String spotifyURL) {
                System.out.println("test12");
                if (spotifyURL.startsWith("https://")) {
                    spotifyURL = spotifyURL.replace("https://", "");
                    System.out.println(spotifyURL);
                }
                if (spotifyURL.startsWith("http://")) {
                    spotifyURL = spotifyURL.replace("http://", "");
                    System.out.println(spotifyURL);
                }
                spotifyURL = spotifyURL.replace("open.spotify.com/", "");
                System.out.println(spotifyURL);


                //if url corresponds to a track, get the name
                if (spotifyURL.startsWith("track/")) {
                    System.out.println(spotifyURL);
                    if (spotifyURL.endsWith("/")) {
                        System.out.println(spotifyURL);
                        spotifyURL.replace("/", "");
                        System.out.println(spotifyURL);
                    }
                    System.out.println(spotifyURL);
                    GetTrackRequest.Builder request = spotifyApi.getTrack(spotifyURL);
                    try {
                        Track returnedTrack = request.build().execute();
                        String trackName = returnedTrack.getName();
                        System.out.println("trackName = " + trackName);
                        System.out.println("returnedTrack.getUri() = " + returnedTrack.getUri());
                        System.out.println("returnedTrack.getPreviewUrl() = " + returnedTrack.getPreviewUrl());


                        //create Linked List
                        retunable.add(trackName);


                        return retunable;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SpotifyWebApiException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                retunable.clear();
                retunable.add("234234NothingFound!hfg23");

                return retunable;

            }


}
