package gq.not11.bot.commands.music;

import gq.not11.bot.core.command.Command;
import gq.not11.bot.core.command.CommandEvent;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public class JoinCommand extends Command {

    public JoinCommand() {
        super("Let's the bot join your channel", new String[]{"join"}, "");
    }


    @Override
    public void run(CommandEvent event) {

        GuildMessageReceivedEvent raw = ((GuildMessageReceivedEvent) event.getRaw());
        Guild guild = raw.getGuild();
        Member member = raw.getMember();
        VoiceChannel vc = member.getVoiceState().getChannel();
        AudioManager audioManager = guild.getAudioManager();

        try {
            audioManager.openAudioConnection(vc);
            raw.getChannel().sendMessage("Successfully joined your Voice Channel!").queue();

        }
        catch (IllegalArgumentException e){
            raw.getChannel().sendMessage("Sorry, but it seems like you aren't connected to a Voice Channel!").queue();

        } return;



    }

}
