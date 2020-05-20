package net.cloudybyte.bot.core.audio;

import com.mongodb.*;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import com.stripe.Stripe;
import com.stripe.model.Customer;
import net.cloudybyte.bot.core.Constants;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

import static net.cloudybyte.bot.util.Colors.*;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private final long guildid;


    private boolean trackLoop = false;
    private boolean queueLoop = false;
    private boolean shuffle = false;

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
        AudioTrack track = queue.poll();
        if (track == null){return;}
       // if(track.getInfo().length > 3558000 && )
        player.startTrack(track, false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)

        MongoClient mongoClient = null;
        mongoClient = new MongoClient(new MongoClientURI(Constants.DBUri));
        DB database = mongoClient.getDB("soundy");
        DBCollection loopingCollection = database.getCollection("trackLooping");
        DBCollection shuffledCollection = database.getCollection("shuffled");

        //search for guild in db
        DBObject query = new BasicDBObject("guildid", guildid);
        //Build selector for the looping query
        BasicDBObject loopingSelect = new BasicDBObject();
        loopingSelect.put("looped", 1);
        DBCursor loopingCursor = loopingCollection.find(query, loopingSelect);

        BasicDBObject loopingObj = (BasicDBObject) loopingCursor.next();
        Integer looped = Integer.parseInt(loopingObj.getString("looped"));


        //Build selector for the looping query
        BasicDBObject shuffleSelect = new BasicDBObject();
        loopingSelect.put("shuffled", 1);
        DBCursor shuffleCursor = loopingCollection.find(query, shuffleSelect);

        BasicDBObject shuffleObj = (BasicDBObject) shuffleCursor.next();
        Integer shuffled = Integer.parseInt(shuffleObj.getString("shuffled"));


        System.out.println(GREEN + "Track ended!" + RESET);
        System.out.println("looped = " + looped);
        System.out.println("shuffled = " + shuffled);


        switch (looped) {
            case 0:
                trackLoop = false;
                queueLoop = false;
                break;

            case 1:
                trackLoop = true;
                queueLoop = false;
                break;
            case 2:
                trackLoop = false;
                queueLoop = true;
                break;
        }

        switch (shuffled){
            case 0:
                shuffle = false;
                break;
            case 1:
                shuffle = true;
        }


        if (endReason.mayStartNext && trackLoop) {
            System.out.println(BLUE + "Track looping enabled!" + RESET);
            player.playTrack(track.makeClone());
        } else if (endReason.mayStartNext && queueLoop) {
            System.out.println(GREEN + "Queue looping enabled!" + RESET);
            queue.add(track.makeClone());
            nextTrack();
        } else if (endReason.mayStartNext && shuffle) {
            System.out.println(GREEN + "Shuffle!" + RESET);
            final int index = ThreadLocalRandom.current().nextInt(getQueue().size());
            AudioTrack nextTrack = ((LinkedList<AudioTrack>) getQueue()).get(index);
            ((LinkedList<AudioTrack>) getQueue()).remove(index);
            player.startTrack(nextTrack, false);
        } else if (endReason.mayStartNext) {
            System.out.println(GREEN + "NEXT TRACK STARTING SOON" + RESET);
            nextTrack();
        }
        mongoClient.close();

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