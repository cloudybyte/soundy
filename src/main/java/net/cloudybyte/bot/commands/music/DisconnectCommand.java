package net.cloudybyte.bot.commands.music;



import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.audio.GuildMusicManager;
import net.cloudybyte.bot.core.audio.PlayerManager;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.cloudybyte.bot.util.Reactions;
import net.dv8tion.jda.api.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

public class DisconnectCommand implements ICommand {





    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        VoiceChannel vc = member.getVoiceState().getChannel();
        AudioManager audioManager = guild.getAudioManager();
        SentryClient sentry = SentryClientFactory.sentryClient();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());




        if (audioManager.getConnectionStatus().equals(ConnectionStatus.NOT_CONNECTED)) {
            embedBuilder.error(event, "Not connected", "I am currently not connected to any voice channel!");
            return;
        }

        if(!(event.getMember().getVoiceState().getChannel() == audioManager.getConnectedChannel())){
            embedBuilder.error(event, "Not connected", "We are not in the same voice channel!");
            return;
        }

        try {
            audioManager.closeAudioConnection();
            event.getMessage().addReaction(Reactions.BYE).queue();
            guildMusicManager.scheduler.getQueue().clear();
            guildMusicManager.player.stopTrack();
            guildMusicManager.player.setPaused(false);
        }
        catch (IllegalArgumentException e){
            sentry.sendException(e);
        }
    }

    @Override
    public String getHelp() {
        return "Disconnects the bot from you channel\n" +
                "Usage: `" + Constants.PREFIX + getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "dc";
    }
}

