package net.cloudybyte.bot.util;

import com.sun.management.OperatingSystemMXBean;
import net.cloudybyte.bot.core.Constants;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.awt.*;
import java.time.Instant;

public class EmbedUtil {

    private static final String URL = Constants.URL;
    private static final String iconURL = Constants.iconURL;
    public JDAInfo jdaInfo = new JDAInfo();
    public JDA jda;
    private ShardManager shardManager;
    private OperatingSystemMXBean operatingSystemMXBean;

    public static MessageEmbed smallinfo(String title, String content) {

        net.dv8tion.jda.api.EmbedBuilder builder = new net.dv8tion.jda.api.EmbedBuilder()
                .setTitle(title)
                .setColor(Role.DEFAULT_COLOR_RAW)
                .setFooter(URL, iconURL);

        StringBuilder descriptionBuilder = builder.getDescriptionBuilder();

        descriptionBuilder.append(content);

        //TODO: Permission check if the bot can send embed messages
        return builder.build();

    }

    public MessageEmbed welcome(GuildJoinEvent event, Guild guild) {

        String title = String.format("**Hey!** Thanks for inviting me!", guild.getJDA().getSelfUser().getName());
        String content = "**-** My prefix is: `" + Constants.PREFIX + "`" + "**-** I got many commands! Request a list by typing: `" + Constants.PREFIX + "help`" + String.format("**-** You wanna start listening to some music right now? Use **`%splay`**", Constants.PREFIX) + "**-** Need help? Join the support server at **https://discord.gg/HXXAqwb**";

        net.dv8tion.jda.api.EmbedBuilder builder = new net.dv8tion.jda.api.EmbedBuilder()
                .setTitle(title)
                .setColor(Color.CYAN)
                .setFooter(URL, iconURL)
                .setTimestamp(Instant.now());

        StringBuilder descriptionBuilder = builder.getDescriptionBuilder();

        descriptionBuilder.append(content);

        //TODO: Permission check if the bot can send embed messages
        return builder.build();

    }


    public MessageEmbed dashboard(GuildMessageReceivedEvent event) {

        String title = "Reaction Dashboard:";
        String content = "Just react with the emojis below:";

        net.dv8tion.jda.api.EmbedBuilder builder = new net.dv8tion.jda.api.EmbedBuilder()
                .setTitle(title)
                .setColor(Color.BLUE)
                .setFooter(URL, iconURL)
                .setTimestamp(Instant.now());

        StringBuilder descriptionBuilder = builder.getDescriptionBuilder();

        descriptionBuilder.append(content);

        //TODO: Permission check if the bot can send embed messages
        return builder.build();

    }
}
