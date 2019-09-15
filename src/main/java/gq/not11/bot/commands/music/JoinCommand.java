package gq.not11.bot.commands.music;


import gq.not11.bot.core.Constants;
import gq.not11.bot.core.command.ICommand;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.GuildUnavailableException;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.core.managers.AudioManager;

import java.util.List;

public class JoinCommand implements ICommand {



    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        VoiceChannel vc = member.getVoiceState().getChannel();
        AudioManager audioManager = guild.getAudioManager();
        SentryClient sentry = SentryClientFactory.sentryClient();

        try {
            audioManager.openAudioConnection(vc);
            event.getChannel().sendMessage("Successfully joined your Voice Channel!").queue();
        }
        catch (IllegalArgumentException e) {
            event.getChannel().sendMessage("Sorry, but it seems like you aren't connected to a Voice Channel!").queue();
        }
        catch (UnsupportedOperationException | GuildUnavailableException e){
            sentry.sendException(e);

        }
        catch (InsufficientPermissionException e){
            sentry.sendException(e);
            event.getChannel().sendMessage("Sorry, but I don't have the proper permission to perform this action or the user limit may be exceeded!").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Lets the bot join your voice Channel\n" +
                "Usage: `" + Constants.PREFIX + getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "join";
    }
}
