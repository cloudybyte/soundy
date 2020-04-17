/*
 * Copyright (c) Ole Donnermeyer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ole Donnermeyer <ole.donnermeyer@gmx.net>, 2020
 */

package net.cloudybyte.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.cloudybyte.bot.core.audio.GuildMusicManager;
import net.cloudybyte.bot.core.audio.PlayerManager;
import net.cloudybyte.bot.core.audio.TrackScheduler;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class SkipCommand implements ICommand {

    EmbedBuilder embedBuilder = new EmbedBuilder();
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;
        AudioPlayer player = musicManager.player;


        if (player.getPlayingTrack() == null) {
            embedBuilder.error(event, null, "* silence *");
            return;
        }
        scheduler.nextTrack();

        embedBuilder.nowPlayingCommand(event, musicManager.player.getPlayingTrack().getInfo().title);

    }

    @Override
    public String getHelp() {
        return "Skips the current song";
    }

    @Override
    public String getInvoke() {
        return "skip";
    }
}
