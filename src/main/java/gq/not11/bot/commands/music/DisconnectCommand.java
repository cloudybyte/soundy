package gq.not11.bot.commands.music;


import gq.not11.bot.core.Constants;
import gq.not11.bot.core.command.ICommand;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

import java.util.List;

public class DisconnectCommand implements ICommand {





    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        VoiceChannel vc = member.getVoiceState().getChannel();
        AudioManager audioManager = guild.getAudioManager();
        SentryClient sentry = SentryClientFactory.sentryClient();

        try {
            audioManager.closeAudioConnection();
            event.getChannel().sendMessage("Disconnected!").queue();
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

