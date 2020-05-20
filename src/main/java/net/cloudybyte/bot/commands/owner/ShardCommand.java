/*
 * Copyright (c) Ole Donnermeyer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ole Donnermeyer <ole.donnermeyer@gmx.net>, 2020
 */

package net.cloudybyte.bot.commands.owner;


import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.command.ICommand;
import net.cloudybyte.bot.util.EmbedBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class ShardCommand implements ICommand {
    SentryClient sentry = SentryClientFactory.sentryClient();

    ShardManager shardManager;
    EmbedBuilder embedBuilder = new EmbedBuilder();


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String args1 = args.get(0);
        String args2 = args.get(2);


        if(event.getMessage().getAuthor().getIdLong() == Constants.OWNER){
            switch (args1) {
                case "restart":

                    if (args2.equals("all")) {
                        shardManager.restart();
                    } else {
                        try {
                            shardManager.restart(Integer.parseInt(args2));
                        } catch (IllegalArgumentException e) {
                            sentry.sendException(e);
                            event.getChannel().sendMessage("You have to provide a valid shard ID!").queue();
                        }
                    }

                    break;
                case "running":
                    embedBuilder.shardNumberRunning(event);
                    break;
                case "ping":
                    embedBuilder.shardAveragePing(event);
                    break;
                case "application":
                    embedBuilder.shardApplicationInfo(event);
                    break;
                case "shutdown":
                    if (args2.isEmpty()) {
                        embedBuilder.error(event, "Argument Error", "You have to provide a shard ID! To shutdown the bot completely please use " + Constants.PREFIX + "shutdown");
                    }
                    try {
                        shardManager.shutdown(Integer.parseInt(args2));
                    } catch (IllegalArgumentException e) {
                        sentry.sendException(e);
                        embedBuilder.error(event, "Argument Error", "Please provide a number in order to specify a shard!");
                    }
                    break;
            }




        }

    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getInvoke() {
        return null;
    }
}
