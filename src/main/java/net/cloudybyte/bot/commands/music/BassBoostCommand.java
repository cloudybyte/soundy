package net.cloudybyte.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.filter.PcmFilterFactory;
import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.cloudybyte.bot.core.audio.GuildMusicManager;
import net.cloudybyte.bot.core.audio.PlayerManager;
import net.cloudybyte.bot.core.audio.TrackScheduler;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class BassBoostCommand implements ICommand {

    private static final float[] BASSBOOST = { 0.2f, 0.15f, 0.1f, 0.05f, 0.0f, -0.05f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f,
            -0.1f, -0.1f, -0.1f, -0.1f };
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;
        AudioPlayer player = musicManager.player;
        EmbedBuilder embedBuilder = new EmbedBuilder();
        EqualizerFactory equalizerFactory = new EqualizerFactory();

        if (args.get(0).equals("off")){
            player.setFilterFactory(null);
            return;
        }else{
            for (int i = 0; i < BASSBOOST.length; i++) {
                equalizerFactory.setGain(i, BASSBOOST[i]);
                System.out.println("i = " + i);
            }
            player.setFilterFactory(equalizerFactory);
            System.out.println("BassBoost on!");
        }




    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getInvoke() {
        return "bb";
    }
}
