package net.cloudybyte.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import net.cloudybyte.bot.core.audio.PlayerManager;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class VolumeCommand implements ICommand {


    long OWNER = Long.parseLong(System.getenv("OWNER"));


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getMessage().getAuthor().getIdLong() == OWNER) {

            AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
            AudioPlayer player = playerManager.createPlayer();


            EmbedBuilder embedBuilder = new EmbedBuilder();


            PlayerManager manager = PlayerManager.getInstance();

            if (args.get(0).isEmpty() || args.get(0).equals(" ")) {
                manager.getGuildMusicManager(event.getGuild()).player.getVolume();
            } else {
                if (Integer.parseInt(args.get(0)) > 0 & Integer.parseInt(args.get(0)) <= 1000) {
                    try {
                        manager.getGuildMusicManager((event.getGuild())).player.setVolume(Integer.parseInt(args.get(0)));
                    } catch (NumberFormatException e) {
                        embedBuilder.error(event, "Not a valid number :(", "Please provide a valid number which is (lower) than 1000 and higher than 0!");
                    }
                }


            }
        }
    }


        @Override
        public String getHelp () {
            return "This command lets you control the volume of your favorite music :)";
        }

        @Override
        public String getInvoke () {
            return "vol";
        }
    }

