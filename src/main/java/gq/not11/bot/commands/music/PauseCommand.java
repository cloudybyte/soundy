package gq.not11.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import gq.not11.bot.core.audio.TrackScheduler;
import gq.not11.bot.core.command.Command;
import gq.not11.bot.core.command.CommandEvent;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public class PauseCommand extends Command {

    public PauseCommand() {
        super("Let's you pause the music", new String[]{"pause"}, ".pause");
    }

    @Override
    public void run(CommandEvent event) {
        GuildMessageReceivedEvent raw = ((GuildMessageReceivedEvent) event.getRaw());

        Guild guild = raw.getGuild();
        Member member = raw.getMember();
        VoiceChannel vc = member.getVoiceState().getChannel();
        AudioManager audioManager = guild.getAudioManager();


        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioPlayer player = playerManager.createPlayer();
        TrackScheduler trackScheduler = new TrackScheduler(player);

        TextChannel channel = raw.getChannel();

    }
}

