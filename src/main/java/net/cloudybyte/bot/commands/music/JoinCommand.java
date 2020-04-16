package net.cloudybyte.bot.commands.music;


import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.GuildUnavailableException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

public class JoinCommand implements ICommand {



    private EmbedBuilder embedBuilder = new EmbedBuilder();





    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();


        joinVC(member, guild, event);
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







    public void joinVC(Member member, Guild guild, GuildMessageReceivedEvent event) {
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
            embedBuilder.error(event, "Error", "An error occurred! Error code: ");

        }
        catch (InsufficientPermissionException e){
            sentry.sendException(e);
            embedBuilder.error(event, "Permission error", "Sorry, but I don't have the proper permission to perform this action or the user limit may be exceeded!");

        }
    }
}

