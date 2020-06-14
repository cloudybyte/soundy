package net.cloudybyte.bot.core.audio.spotify;


public class PlaylistInfo {
    String name;
    String thumbnail;
    String owner;
    String description;
    int tracknumber;


    public PlaylistInfo(String nameInput, String thumbnailInput, String ownerInput, String descriptionInput, int tracknumberInput) {
        name = nameInput;
        thumbnail = thumbnailInput;
        owner = ownerInput;
        description = descriptionInput;
        tracknumber = tracknumberInput;
    }


    public String getName(PlaylistInfo playlistInfo) {return playlistInfo.name;}

    public String getOwner(PlaylistInfo playlistInfo) {return playlistInfo.owner;}

    public String getThumbnail(PlaylistInfo playlistInfo) {
        return playlistInfo.thumbnail;
    }

    public String getDescription(PlaylistInfo playlistInfo) {return playlistInfo.description;}

    public int getTracknumber(PlaylistInfo playlistInfo) {return playlistInfo.tracknumber;}
}
