package net.cloudybyte.bot.commands.premium;

import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.core.data.cache.AlwaysOnCache;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.cloudybyte.bot.util.Reactions;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class AlwaysOnCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
         //Owner Exception
        if (event.getAuthor().getIdLong() == Constants.OWNER){
            alwaysOnCommand(event, args);
        }


        //Partnership Special Permissions -- VOLT EUROPA
        if (event.getGuild().getId().equals("567021913210355745")){
            Role voltServerTeam = event.getGuild().getRoleById("674583505446895616");
        if (event.getGuild().getMemberById(event.getAuthor().getId()).getRoles().contains(voltServerTeam) || event.getAuthor().getIdLong() == Constants.OWNER){
            alwaysOnCommand(event, args);
        }
        }

    }

    @Override
    public String getHelp() {
        return "Triggers AlwaysOn Mode";
    }

    @Override
    public String getInvoke() {
        return "alwayson";
    }
    private void alwaysOnCommand(GuildMessageReceivedEvent event, List<String> args){

        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (args.isEmpty()){
            event.getMessage().getChannel().sendMessage(AlwaysOnCache.get(event.getGuild().getId())).queue();
            return;
        }
        switch (args.get(0)) {
            case "on":
                AlwaysOnCache.put(event.getGuild().getId(), "on");

                event.getMessage().addReaction(Reactions.CHECK_MARK).queue();
                break;

            case "off":
                AlwaysOnCache.put(event.getGuild().getId(), "off");
                event.getMessage().addReaction(Reactions.CHECK_MARK).queue();

                break;


            default:
                embedBuilder.error(event, "Error while triggering AlwaysOn Mode", "Thats not 'on' or 'off' at all");
                event.getMessage().addReaction(Reactions.CROSS).queue();
                break;
        }
    }
}


