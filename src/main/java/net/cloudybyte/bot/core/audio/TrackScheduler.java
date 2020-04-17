package net.cloudybyte.bot.core.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.data.MySQLManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.ResultSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static net.cloudybyte.bot.util.Colors.*;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter{

    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private final long guildid;
    AudioTrack lastTrack;
    MySQLManager mySQLManager = new MySQLManager("92.60.39.215", "3306", "soundy", "soundy_i_c_!", "soundy");

    private boolean trackLoop = false;
    private boolean queueLoop = false;

    /**
     * @param player The audio player this scheduler uses
     */
    public TrackScheduler(AudioPlayer player, long guildid) {
        this.player = player;
        this.guildid = guildid;
        this.queue = new LinkedBlockingQueue<>();
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track) {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack() {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        player.startTrack(queue.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
       // mySQLManager.connect();

        System.out.println(GREEN + "Track ended!" + RESET);
        System.out.println(GREEN + "trackLoop = " + trackLoop + RESET);

        try {

            mySQLManager.connect();
            ResultSet resultSet = mySQLManager.select(new String[]{"looped"}, "tracklooping", "guildid == '" + guildid + "' ", 1, null);

            Integer[] loopingArray = mySQLManager.getInts(resultSet, "looped");
            Integer looping = loopingArray[0];

            if (looping.equals(0)){
                trackLoop = false;
                queueLoop = false;
            }
            if (looping.equals(1)){
                trackLoop = true;
                queueLoop = false;
            }
            if (looping.equals(2)){
                trackLoop = false;
                queueLoop = true;
            }


            if(endReason.mayStartNext && trackLoop){
                System.out.println(BLUE + "Track looping enabled!" + RESET );
                player.playTrack(track.makeClone());
            } else if (endReason.mayStartNext && queueLoop) {
                System.out.println(GREEN + "Queue looping enabled!" + RESET);
                queue(track.makeClone());
            } else if (endReason.mayStartNext) {
                System.out.println(GREEN + "NEXT TRACK STARTING SOON" + RESET);
                nextTrack();
            }

        } catch (MySQLManager.MySQL_NotConnectedQueryException e) {
            e.printStackTrace();
        } catch (MySQLManager.MySQL_WrongDataTypeException e) {
            e.printStackTrace();
        } catch (MySQLManager.MySQL_NoEntryFoundException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }


        public void skipTrack() {
            //Starting the track with noInterrupt set to true will stop the current track and start the next one
            player.startTrack(queue.poll(), true);
        }



        public boolean isTrackLooped() {
        return trackLoop;
    }

        public boolean isQueueLooped() {
        return queueLoop;
        }
}