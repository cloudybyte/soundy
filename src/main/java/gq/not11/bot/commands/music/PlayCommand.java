package gq.not11.bot.commands.music;


import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import gq.not11.bot.core.audio.AudioPlayerSendHandler;
import gq.not11.bot.core.command.Command;
import gq.not11.bot.core.command.CommandEvent;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.core.managers.AudioManager;

public class PlayCommand extends Command implements AudioEventListener {

    public PlayCommand() {
        super("Let's you play music", new String[]{"play"}, ".play [provide youtube link]");
    }




    @Override
    public void run(CommandEvent event) {

        GuildMessageReceivedEvent raw = ((GuildMessageReceivedEvent) event.getRaw());

        Guild guild = raw.getGuild();
        Member member = raw.getMember();
        VoiceChannel vc = member.getVoiceState().getChannel();
        AudioManager audioManager = guild.getAudioManager();
        AudioManager manager = guild.getAudioManager();

        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioPlayer player = playerManager.createPlayer();


        manager.setSendingHandler(new AudioSendHandler() {
            @Override
            public boolean canProvide() {
                return false;
            }

            @Override
            public byte[] provide20MsAudio() {
                return new byte[0];
            }
        });
        try{
            manager.openAudioConnection(vc);

        } catch (IllegalArgumentException e) {
            raw.getChannel().sendMessage("Oh looks like you aren't connected to a Voice Channel!").queue();
        }
        catch (UnsupportedOperationException e){
            raw.getChannel().sendMessage("Oh no something went wrong: UnsupportedOperationException! Please contact noteleven#9301").queue();
        }
        catch (InsufficientPermissionException e) {
            raw.getChannel().sendMessage("Looks like I don't have the proper permission to join your Voice Channel! Please check them and try again!").queue();
        }











        raw.getChannel().sendMessage("This is the play command").queue();

        raw.getMessage().addReaction("\u25B6").queue();
        System.out.println("Play Command was used!");



        }

    @Override
    public void onEvent(AudioEvent event) {

    }
}

