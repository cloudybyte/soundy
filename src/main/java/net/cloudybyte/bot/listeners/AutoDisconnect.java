package net.cloudybyte.bot.listeners;

import net.cloudybyte.bot.core.audio.GuildMusicManager;
import net.cloudybyte.bot.core.audio.PlayerManager;
import net.cloudybyte.bot.core.data.cache.AlwaysOnCache;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

public class AutoDisconnect extends ListenerAdapter {
    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {

            if (AlwaysOnCache.get(event.getGuild().getId()).equals("on")){
                return;
            }


            if (event.getChannelLeft().getMembers().size() == 1) {

                AudioManager audioManager = event.getGuild().getAudioManager();
                PlayerManager playerManager = PlayerManager.getInstance();
                GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());

                //reset player
                guildMusicManager.scheduler.getQueue().clear();
                guildMusicManager.player.stopTrack();
                guildMusicManager.player.setPaused(false);

                //disconnect from vc
                audioManager.closeAudioConnection();


            }

    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        if (AlwaysOnCache.get(event.getGuild().getId()).equals("on")){
            return;
        }
            if (event.getChannelLeft().getMembers().size() == 1) {
                AudioManager audioManager = event.getGuild().getAudioManager();
                PlayerManager playerManager = PlayerManager.getInstance();
                GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());

                //reset player
                guildMusicManager.scheduler.getQueue().clear();
                guildMusicManager.player.stopTrack();
                guildMusicManager.player.setPaused(false);

                //disconnect from vc
                audioManager.closeAudioConnection();
            }

    }
}
