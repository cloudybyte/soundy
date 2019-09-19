package gq.not11.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import gq.not11.bot.core.audio.GuildMusicManager;
import gq.not11.bot.core.audio.PlayerManager;
import gq.not11.bot.core.audio.TrackScheduler;
import gq.not11.bot.core.command.ICommand;
import gq.not11.bot.util.EmbedBuilder;
import gq.not11.bot.util.Reactions;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

import java.util.List;

public class PauseCommand implements ICommand {





    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if(guildMusicManager.player.isPaused()) {
            embedBuilder.info(event, "Resuming", "Whoops! I can't pause the music because it was already paused, so I resumed it for you!");
            event.getMessage().addReaction(Reactions.PLAY).queue();
            guildMusicManager.player.setPaused(false);

        }
        else if (!guildMusicManager.player.isPaused()) {
            event.getMessage().addReaction(Reactions.PAUSE).queue();
            guildMusicManager.player.setPaused(true);

        }
    }

    @Override
    public String getHelp() {
        return "Let's you pause the music";
    }

    @Override
    public String getInvoke() {
        return "pause";
    }
}

