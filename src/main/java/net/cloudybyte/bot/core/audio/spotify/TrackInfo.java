package net.cloudybyte.bot.core.audio.spotify;

import javax.sound.midi.Track;

public class TrackInfo {
    String trackName;
    String trackArtist;
    int duration;
    int popularity;


    public TrackInfo(String trackNameInput, String trackArtistInput, int durationInput, int popularityInput)
    {
        trackArtist = trackArtistInput; trackName = trackNameInput; duration = durationInput; popularity = popularityInput;
    }


    public String getName(TrackInfo trackInfo){
        return trackInfo.trackName;
    }
    public String getArtist(TrackInfo trackInfo){
        return trackInfo.trackArtist;
    }

    public int getDuration(TrackInfo trackInfo) {
        return trackInfo.duration;
    }

    public int getPopularity(TrackInfo trackInfo){
        return trackInfo.popularity;
    }
}
