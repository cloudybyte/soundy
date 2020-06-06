package net.cloudybyte.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.audio.GuildMusicManager;
import net.cloudybyte.bot.core.audio.PlayerManager;
import net.cloudybyte.bot.core.audio.TrackScheduler;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.Colors;
import net.cloudybyte.bot.util.FormatUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
public class QueueListCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;
        AudioPlayer player = musicManager.player;
        int queueSize = scheduler.getQueue().size();




        if (queueSize == 0) {
            event.getChannel().sendMessage("```md\n\n< The queue is empty :)  If you want to add more just use " + Constants.PREFIX + "play >\n```").queue();
            return;
        }

        Collection<AudioTrack> queueList = new LinkedList<>(scheduler.getQueue());



        if (scheduler.getQueue().size() <= 10){
            int i = 0;
            StringBuilder queueMessage = new StringBuilder();
            queueMessage.append("```md\n");
            queueMessage.append("#Your queue:\n\n");
            forEach:
            for (AudioTrack track : queueList) {
                if (i < 8) {
                    queueMessage.append("[" + i++ + "](" + track.getInfo().title + ")  <" + FormatUtil.formatTimestamp(track.getInfo().length) + ">\n");
                    System.out.println(Colors.CYAN + track.getInfo().title + Colors.RESET);
                }else{
                    break;
                }
            }
            queueMessage.append("\n");
            if(queueList.size() > 8){
                queueMessage.append("");
            }
            queueMessage.append("That's the queue! If you want to add more just use " + Constants.PREFIX + "play\n");
            queueMessage.append("```");
            String msg = queueMessage.toString();
            Message queueMessageEntity =    event.getMessage().getChannel().sendMessage(msg).complete();

        }







/*
        int i = 1;

        if (player.getPlayingTrack() == null) {
            StringBuilder queueMessage = new StringBuilder();
            queueMessage.append("```md\n");
            queueMessage.append("#Your queue:\n\n");
            for (AudioTrack track : queueList) {
                queueMessage.append("[" + i++ + "](" + track.getInfo().title + ")  <" + FormatUtil.formatTimestamp(track.getInfo().length) + ">\n");
                System.out.println(Colors.CYAN + track.getInfo().title + Colors.RESET);
            }
            queueMessage.append("\n");
            queueMessage.append("That's the queue! If you want to add more just use " + Constants.PREFIX + "play\n");
            queueMessage.append("```");
            String msg = queueMessage.toString();

            event.getMessage().getChannel().sendMessage(msg).queue();
            return;
        }





        StringBuilder queueMessage = new StringBuilder();
        queueMessage.append("```md\n");
        queueMessage.append("#Your queue:\n\n");

        queueMessage.append("[" + Reactions.PLAY + "](" + player.getPlayingTrack().getInfo().title + ")  <" + FormatUtil.formatTimestamp(player.getPlayingTrack().getInfo().length) + ">" + "\n");



        for (AudioTrack track : queueList) {


            queueMessage.append("[" + i++ + "](" + track.getInfo().title + ")  <" + FormatUtil.formatTimestamp(track.getInfo().length) + ">\n");
            System.out.println(Colors.CYAN + track.getInfo().title + Colors.RESET);
        }

        queueMessage.append("\n");
        queueMessage.append("This was the queue! If you want to add more just use " + Constants.PREFIX + "play\n");
        queueMessage.append("```");
        String msg = queueMessage.toString();

        event.getMessage().getChannel().sendMessage(msg).queue();*/


    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getInvoke() {
        return "queue";
    }

}