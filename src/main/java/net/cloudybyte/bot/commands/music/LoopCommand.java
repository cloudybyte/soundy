package net.cloudybyte.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.audio.TrackScheduler;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.core.data.MySQLManager;
import net.cloudybyte.bot.util.Colors;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.cloudybyte.bot.util.GuildTrackScheduleHandler;
import net.cloudybyte.bot.util.Reactions;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.List;

import static net.cloudybyte.bot.util.Colors.*;

public class LoopCommand implements ICommand {
    MySQLManager mySQLManager = new MySQLManager("92.60.39.215", "3306", "soundy", "soundy_i_c_!", "soundy");

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        System.out.println(BLUE + "LoopCommand triggered!" + RESET);
        SentryClient sentry = SentryClientFactory.sentryClient();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        VoiceChannel vc = event.getMember().getVoiceState().getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();



        /*
        *   Values and meanings:
        *   0 -> not looped
        *   1 -> track looped
        *   2 -> queue looped
        */

        
        mySQLManager.connect();
        try {
            ResultSet resultSet = mySQLManager.select(new String[]{"looped"}, "tracklooping", "guildid == '" + event.getGuild().getIdLong() + "' ", 1, null);
            Integer[] loopingArray = mySQLManager.getInts(resultSet, "looped");
            Integer looping = loopingArray[0];
        } catch (MySQLManager.MySQL_NotConnectedQueryException e) {
            embedBuilder.error(event, "DB Error", "There was an internal DB error, please join the support server to get additional support. Errorcode: 1");
        } catch (MySQLManager.MySQL_WrongDataTypeException e) {
            embedBuilder.error(event, "DB Error", "There was an internal DB error, please join the support server to get additional support. Errorcode: 2");
        } catch (MySQLManager.MySQL_NoEntryFoundException e) {
            embedBuilder.error(event, "DB Error", "There was an internal DB error, please join the support server to get additional support. Errorcode: 3");
        }
        catch (ArrayIndexOutOfBoundsException e){
            try {
                mySQLManager.insert("tracklooping", new String[]{"guildid", "looped"}, new Object[]{event.getGuild().getIdLong(), 0});

            } catch (MySQLManager.MySQL_NotConnectedQueryException mySQL_notConnectedQueryException) {
                embedBuilder.error(event, "DB Error", "There was an internal DB error, please join the support server to get additional support. Errorcode: 4");
            }
        }

        try {
            ResultSet resultSet = mySQLManager.select(new String[]{"looped"}, "tracklooping", "guildid == '" + event.getGuild().getIdLong() + "' ", 1, null);
            Integer[] loopingArray = mySQLManager.getInts(resultSet, "looped");
            try {
                Integer looping = loopingArray[0];
                if (looping.equals(0) && args.isEmpty()) {
                    mySQLManager.update("tracklooping", new String[]{"guildid", "looped"}, new Object[]{event.getGuild().getIdLong(), 1}, "guildid == '" + event.getGuild().getIdLong() + "' ");
                    event.getMessage().addReaction(Reactions.REPEAT_ONE).queue();
                    return;
                }
                if (looping.equals(1) && args.isEmpty()) {
                    mySQLManager.update("tracklooping", new String[]{"guildid", "looped"}, new Object[]{event.getGuild().getIdLong(), 0}, "guildid == '" + event.getGuild().getIdLong() + "' ");
                    event.getMessage().addReaction(Reactions.ARROW_FORWARD).queue();
                }
            } catch (ArrayIndexOutOfBoundsException e){
                System.out.println(RED + "ARRAY OUT OF BOUNDS EXCEPTION" + RESET);
            }






        } catch (MySQLManager.MySQL_NotConnectedQueryException e) {
            embedBuilder.error(event, "DB Error", "There was an internal DB error, please join the support server to get additional support. Errorcode: 5");
        } catch (MySQLManager.MySQL_WrongDataTypeException e) {
            embedBuilder.error(event, "DB Error", "There was an internal DB error, please join the support server to get additional support. Errorcode: 6");
        } catch (MySQLManager.MySQL_NoEntryFoundException e) {
            embedBuilder.error(event, "DB Error", "There was an internal DB error, please join the support server to get additional support. Errorcode: 7");
        }
        catch (ArrayIndexOutOfBoundsException e){
                embedBuilder.error(event, "DB Error", "There was an internal DB error, please join the support server to get additional support. Errorcode: 8");
        }











    /*    AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioPlayer player = playerManager.createPlayer();
        TrackScheduler trackScheduler = GuildTrackScheduleHandler.getTrackScheduler(event, event.getGuild());
        System.out.println("number one");
        if (trackScheduler == null) return;
        System.out.println("number two");
        boolean trackLoop = trackScheduler.isTrackLooped();
        System.out.println(RED + "2" + RESET);

          if (!trackLoop) {
            System.out.println(PURPLE + trackScheduler.isTrackLooped() + RESET);
            trackScheduler.setTrackLoop(true);
            System.out.println(RED + "3" + RESET);
            System.out.println(PURPLE + trackScheduler.isTrackLooped() + RESET);
            embedBuilder.info(event, "Looping turned on", "Hey! I just stepped onto the loop train!");
        } else {
            System.out.println(PURPLE + trackScheduler.isTrackLooped() + RESET);
            trackScheduler.setTrackLoop(false);
            System.out.println(RED + "4" + RESET);
            System.out.println(PURPLE + trackScheduler.isTrackLooped() + RESET);
            embedBuilder.info(event, "Looping turned off", "Ouch! I just fell off the looping train!");
        }
            */
    }

    @Override
    public String getHelp() {
        return "lets you loop the playing song";
    }

    @Override
    public String getInvoke() {
        return "loop";
    }

}