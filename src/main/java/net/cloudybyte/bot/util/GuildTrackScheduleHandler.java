package net.cloudybyte.bot.util;

import net.cloudybyte.bot.core.audio.TrackScheduler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.HashMap;

public class GuildTrackScheduleHandler {

    private static HashMap<Long, TrackScheduler> schedulers = new HashMap<>();

    public static boolean addTrackScheduler(Guild guild, TrackScheduler ts) {
        long guildid = guild.getIdLong();
        if (schedulers.containsKey(guildid) || schedulers.containsValue(ts)) return false;
        System.out.println("guildid = " + guildid);
        schedulers.put(guildid, ts);
        return true;
    }

    public static boolean removeTrackScheduler(Guild guild) {
        long guildid = guild.getIdLong();
        if (!schedulers.containsKey(guildid)) return false;
        schedulers.remove(guildid);
        return true;
    }

    public static TrackScheduler getTrackScheduler(GuildMessageReceivedEvent event, Guild guild) {
        long guildid = guild.getIdLong();
        if (schedulers.containsKey(guildid)) {
            Guild returnguild = event.getJDA().getGuildById(guildid);
            return schedulers.get(returnguild);
        }
        return null;
    }

}