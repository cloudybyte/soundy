package net.cloudybyte.bot.commands.owner;


import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.core.premiumcheck.PremiumTierChecker;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class PremiumCheckCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        System.out.println("triggered!");
        PremiumTierChecker premiumTierChecker = new PremiumTierChecker();
        int returnvalue = premiumTierChecker.getPremiumTier(event.getAuthor().getIdLong(), event.getGuild().getIdLong());
        System.out.println(returnvalue);
        if(returnvalue == -5){
            event.getChannel().sendMessage("An error occured while checking your subscription status: Method ran without a response").queue();
        }
        if(returnvalue == 0){
            event.getChannel().sendMessage("You do not have an active subscription!").queue();
        }
        if(returnvalue == -1){
            event.getChannel().sendMessage("We are very sorry, but there was an error verifying your subscription status: No database entry!").queue();
        }
        if(returnvalue == -4){
            event.getChannel().sendMessage("An error occured while checking your subscription status:\n You have more than one active subscripton. Please contact a staff member on the official support server to resolve this problem!").queue();
        }
        if(returnvalue == -3){
            event.getChannel().sendMessage("An error occured while checking your subscription status:\n A security check failed! Did you try to trick our system? Code -3").queue();
        }
        if(returnvalue == 1){
            event.getChannel().sendMessage("You have an active soundy Premium subscription!").queue();
        }
        if(returnvalue == 2){
            event.getChannel().sendMessage("You have an active soundy Gold subscription!").queue();
        }


        event.getChannel().sendMessage("returnvalue: " + returnvalue).queue();
    }

    @Override
    public String getHelp() {
        return "Let's you check your current subscription status";
    }

    @Override
    public String getInvoke() {
        return "checksub";
    }


}
