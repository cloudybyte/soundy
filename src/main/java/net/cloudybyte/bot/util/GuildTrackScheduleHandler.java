package net.cloudybyte.bot.util;

import net.cloudybyte.bot.core.audio.TrackScheduler;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;

public class GuildTrackScheduleHandler {

    private static HashMap<Guild, TrackScheduler> schedulers = new HashMap<>();

    public static boolean addTrackScheduler(Guild guild, TrackScheduler ts) {
        if(schedulers.containsKey(guild) || schedulers.containsValue(ts)) return false;
        schedulers.put(guild, ts);
        return true;
    }

    public static boolean removeTrackScheduler(Guild guild) {
        if(!schedulers.containsKey(guild)) return false;
        schedulers.remove(guild);
        return true;
    }

    public static TrackScheduler getTrackScheduler(Guild guild) {
        if(schedulers.containsKey(guild)) {
            return schedulers.get(guild);
        }
        return null;
    }

}
