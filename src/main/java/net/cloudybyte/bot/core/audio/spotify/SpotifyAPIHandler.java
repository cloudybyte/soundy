package net.cloudybyte.bot.core.audio.spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.exceptions.detailed.BadRequestException;
import com.wrapper.spotify.exceptions.detailed.NotFoundException;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.audio.spotify.auth.ClientCredencials;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.LinkedList;

import static net.cloudybyte.bot.core.audio.spotify.auth.ClientCredencials.clientCredentials_Async;

public class SpotifyAPIHandler {
    // For all requests an access token is needed
    SpotifyApi spotifyApi = ClientCredencials.clientCredentials_Sync();


    /*
     *           _             _ _     _
     *          | |           | (_)   | |
     *     _ __ | | __ _ _   _| |_ ___| |_
     *    | '_ \| |/ _` | | | | | / __| __|
     *    | |_) | | (_| | |_| | | \__ | |_
     *    | .__/|_|\__,_|\__, |_|_|___/\__|
     *    | |             __/ |
     *    |_|            |___/
     */


    public LinkedList<TrackInfo> getPlaylistMap(String playlistURL) {
        clientCredentials_Async();
        String playlistID = null;

        if (playlistURL.startsWith("https://open.spotify.com/track/")) {
            return null;
        }

        if (playlistURL.startsWith("https://open.spotify.com/playlist/")) {
            if (playlistURL.length() >= 53) {
                playlistID = playlistURL.substring(34, 56);

                Playlist playlist = null;
                try {
                    playlist = spotifyApi.getPlaylist(playlistID).build().execute();
                } catch (IOException | SpotifyWebApiException | ParseException e) {
                    e.printStackTrace();
                }


                int i = 0;

                LinkedList<TrackInfo> trackMap = new LinkedList<>();

                while (i < playlist.getTracks().getItems().length) {
                    Track track = ((Track) playlist.getTracks().getItems()[i].getTrack());

                    StringBuilder artists = new StringBuilder();
                    int i2 = 1;
                    for (ArtistSimplified artist : track.getArtists()) {
                        artists.append(artist.getName());
                        System.out.println("i = " + i2);
                        if (!(track.getArtists().length == i2)) {
                            artists.append(", ");
                        }
                        i2++;

                    }

                    TrackInfo trackInfo = new TrackInfo(track.getName(), artists.toString(), (track.getDurationMs() / 1000), track.getPopularity());

                    trackMap.add(trackInfo);
                    i++;
                }

                for (TrackInfo trackInfo : trackMap) {
                    System.out.println(trackInfo.trackName + " - " + trackInfo.trackArtist + ";   Duration: " + trackInfo.duration + ";   Popularity: " + trackInfo.popularity);
                }

                return trackMap;
            }
        }
        return null;
    }


    public PlaylistInfo getPlaylistInfo(String playlistURL) {
        clientCredentials_Async();
        String playlistID = null;

        if (playlistURL.startsWith("https://open.spotify.com/track/")) {
            return null;
        }

        if (playlistURL.startsWith("https://open.spotify.com/playlist/")) {
                if (playlistURL.length() >= 53) {
                    playlistID = playlistURL.substring(34, 56);
                    Playlist playlist = null;
                    try {
                        playlist = spotifyApi.getPlaylist(playlistID).build().execute();
                    } catch (IOException | SpotifyWebApiException | ParseException e) {
                        e.printStackTrace();
                    }
                    PlaylistInfo playlistInfo = new PlaylistInfo(playlist.getName(), playlist.getImages()[0].getUrl(), playlist.getOwner().getDisplayName(), playlist.getDescription(), playlist.getTracks().getTotal());
                    return playlistInfo;
                }
        }

        return null;
    }


    /*
     *      _                  _
     *    | |                | |
     *    | |_ _ __ __ _  ___| | __
     *   | __| '__/ _` |/ __| |/ /
     *   | |_| | | (_| | (__|   <
     *   \__|_|  \__,_|\___|_|\_\
     */


    public TrackInfo getTrackInfo(String songURL) {
        clientCredentials_Async();
        try {
            String trackID = null;

            if (!songURL.startsWith("https://open.spotify.com/track/")) {
                return null;
            }


            if (songURL.startsWith("https://open.spotify.com/track/")) {
                if (songURL.length() >= 53) {
                    trackID = songURL.substring(31, 53);
                }
            }

            if (trackID == null) {
                return null;
            }

            Track track = spotifyApi.getTrack(trackID).build().execute();
            StringBuilder artists = new StringBuilder();
            int i = 1;
            for (ArtistSimplified artist : track.getArtists()) {
                artists.append(artist.getName());
                if (!(track.getArtists().length == i)) {
                    artists.append(", ");
                }
                i++;
            }
            return new TrackInfo(track.getName(), artists.toString(), track.getDurationMs(), track.getPopularity());


        }catch (BadRequestException e){
            return null;
        }
       catch (ParseException | SpotifyWebApiException | IOException e) {
            e.printStackTrace();
        }


        return null;

    }


}
