package net.cloudybyte.bot.commands.music;



import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.Reactions;
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

        try {
            audioManager.closeAudioConnection();
            event.getMessage().addReaction(Reactions.BYE).queue();
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

