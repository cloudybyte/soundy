package net.cloudybyte.bot.util;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class FormatUtil {
    public static String formatProgressBar(long progress, long full) {
        double percentage = (double) progress / full;
        StringBuilder progressBar = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            if ((int) (percentage * 20) == i)
                progressBar.append("\uD83D\uDD18");
            else
                progressBar.append("▬");
        }
        return progressBar.toString();
    }
    public static String formatTimestamp(long millis) {
        long seconds = millis / 1000;
        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }
    public static String getThumbnail(AudioTrack track) {
        return String.format("https://img.youtube.com/vi/%s/default.jpg", track.getIdentifier());
    }
}
