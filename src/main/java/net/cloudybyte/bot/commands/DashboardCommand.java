package net.cloudybyte.bot.commands;

import com.google.common.eventbus.EventBus;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import net.cloudybyte.bot.core.audio.GuildMusicManager;
import net.cloudybyte.bot.core.audio.PlayerManager;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.cloudybyte.bot.util.EmbedUtil;
import net.cloudybyte.bot.util.Reactions;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class DashboardCommand implements ICommand {

    EmbedUtil embedUtil = new EmbedUtil();
    EventWaiter eventWaiter;
    ButtonMenu buttonMenu;
    ButtonMenu.Builder buttonMenuBuilder;


    public DashboardCommand(EventWaiter passedEventWaiter) {
        this.eventWaiter = passedEventWaiter;
    }


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());

        MessageEmbed dashboardMessage = embedUtil.dashboard(event);
        String dbID = event.getChannel().sendMessage(dashboardMessage).complete().getId();
        Message msg = event.getChannel().retrieveMessageById(dbID).complete();
        msg.addReaction(Reactions.EXCLAMATION).queue();
        buttonMenuBuilder = new ButtonMenu.Builder();
        buttonMenuBuilder.setEventWaiter(eventWaiter);
        ButtonMenu.Builder builder = buttonMenuBuilder.addChoice(Reactions.EXCLAMATION);
        builder.setText("?");
        MessageReaction.ReactionEmote exclamation = MessageReaction.ReactionEmote.fromUnicode(Reactions.EXCLAMATION, event.getJDA());




        ButtonMenu menu = builder.build();
        menu.display(event.getChannel());

       /* eventWaiter.waitForEvent(GuildMessageReactionAddEvent.class, e -> e.getReaction().equals(e.getReaction()), e -> {

            if (!e.getReaction().retrieveUsers().complete().contains(e.getJDA().getSelfUser())){
                return;
            }
            System.out.println("lel ! Triggered!");

            if (e.getReactionEmote().equals(Reactions.PLAY)){
                guildMusicManager.player.setPaused(false);
            }else if (e.getReactionEmote().equals(Reactions.PAUSE)){
                guildMusicManager.player.setPaused(true);
            }else if (e.getReactionEmote().equals(Reactions.EXCLAMATION)){
                e.getChannel().sendMessage("yea").queue();
            }else{
                e.getChannel().sendMessage("nothing found!").queue();
            }
        }   );*/

    }

    @Override
    public String getHelp() {
        return "soundys reaction dashboard";
    }

    @Override
    public String getInvoke() {
        return "db";
    }
}
