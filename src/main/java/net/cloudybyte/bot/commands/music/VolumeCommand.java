package net.cloudybyte.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import net.cloudybyte.bot.core.audio.PlayerManager;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.cloudybyte.bot.util.GuildVolumeHandler;
import net.cloudybyte.bot.util.Reactions;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.net.UnknownHostException;
import java.util.List;

public class VolumeCommand implements ICommand {


    long OWNER = Long.parseLong(System.getenv("OWNER"));


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getMessage().getAuthor().getIdLong() == OWNER || event.getMessage().getAuthor().getIdLong() == 318750418316296195L) {

            AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
            AudioPlayer player = playerManager.createPlayer();
            GuildVolumeHandler guildVolumeHandler = new GuildVolumeHandler();


            EmbedBuilder embedBuilder = new EmbedBuilder();


            PlayerManager manager = PlayerManager.getInstance();

            if (args.isEmpty() || args.get(0).equals(" ")) {
                manager.getGuildMusicManager(event.getGuild()).player.getVolume();

            } else {
                try {
                    if (Integer.parseInt(args.get(0)) > 0 & Integer.parseInt(args.get(0)) <= 300) {
                        try {
                            manager.getGuildMusicManager((event.getGuild())).player.setVolume(Integer.parseInt(args.get(0)));
                            guildVolumeHandler.setVolume(event.getGuild(), Integer.valueOf(args.get(0)));
                            event.getMessage().addReaction(Reactions.OK_HAND).queue();
                        } catch (NumberFormatException e) {
                            embedBuilder.error(event, "Not a valid number :(", "Please provide a valid number which is (lower than) 300 and higher than 0!");
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (NumberFormatException e) {
                    embedBuilder.error(event, "Not a valid number :(", "Please provide a valid number which is (lower than) 300 and higher than 0!");
                }


            }
        }
    }


    @Override
    public String getHelp() {
        return "This command lets you control the volume of your favorite music :)";
    }

    @Override
    public String getInvoke() {
        return "vol";
    }
}

