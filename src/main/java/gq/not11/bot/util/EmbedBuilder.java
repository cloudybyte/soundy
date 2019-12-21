package gq.not11.bot.util;

import com.sun.management.OperatingSystemMXBean;
import gq.not11.bot.Bot;
import gq.not11.bot.commands.UptimeCommand;
import gq.not11.bot.core.Constants;
import gq.not11.bot.core.audio.GuildMusicManager;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDAInfo;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

import java.time.Instant;

public class EmbedBuilder{

    public JDAInfo jdaInfo = new JDAInfo();
    private ShardManager shardManager;
    private OperatingSystemMXBean operatingSystemMXBean;
    public JDA jda;











    public void info(GuildMessageReceivedEvent event, String title, String content) {

        net.dv8tion.jda.core.EmbedBuilder builder = new net.dv8tion.jda.core.EmbedBuilder()
                .setTitle(title)
                .setColor(Color.white)
                .setFooter("https://nightplay.gq", null)
                .setTimestamp(Instant.now());

        StringBuilder descriptionBuilder = builder.getDescriptionBuilder();

        descriptionBuilder.append(content);

        //TODO: Permission check if the bot can send embed messages
        event.getChannel().sendMessage(builder.build()).queue();

    }


    public void success(GuildMessageReceivedEvent event, String title, String content) {

        net.dv8tion.jda.core.EmbedBuilder builder = new net.dv8tion.jda.core.EmbedBuilder()
                .setTitle(title)
                .setColor(Color.green)
                .setFooter("https://nightplay.gq", null)
                .setTimestamp(Instant.now());

        StringBuilder descriptionBuilder = builder.getDescriptionBuilder();

        descriptionBuilder.append(content);

        //TODO: Permission check if the bot can send embed messages
        event.getChannel().sendMessage(builder.build()).queue();

    }

    public void warn(GuildMessageReceivedEvent event, String title, String content) {

        net.dv8tion.jda.core.EmbedBuilder builder = new net.dv8tion.jda.core.EmbedBuilder()
                .setTitle(title)
                .setColor(Color.orange)
                .setFooter("https://nightplay.gq", null)
                .setTimestamp(Instant.now());

        StringBuilder descriptionBuilder = builder.getDescriptionBuilder();

        descriptionBuilder.append(content);

        //TODO: Permission check if the bot can send embed messages
        event.getChannel().sendMessage(builder.build()).queue();

    }

    public void error(GuildMessageReceivedEvent event, String title, String content) {

        net.dv8tion.jda.core.EmbedBuilder builder = new net.dv8tion.jda.core.EmbedBuilder()
                .setTitle(title)
                .setColor(Color.red)
                .setFooter("https://nightplay.gq", null)
                .setTimestamp(Instant.now());

        StringBuilder descriptionBuilder = builder.getDescriptionBuilder();

        descriptionBuilder.append(content);

        //TODO: Permission check if the bot can send embed messages
        event.getChannel().sendMessage(builder.build()).queue();

    }

    public void welcome(GuildMessageReceivedEvent event, Guild guild) {

        String title = String.format("**Hey!** My name is %s and I am the best music bot on Discord! **Seriously!**", guild.getJDA().getSelfUser().getName());
        String content = String.format("**-** My prefix is: `" + Constants.PREFIX + "`") + String.format("**-** I got many commands! Request a list by typing: `" + Constants.PREFIX + "help`") + String.format("**-** You wanna start listening to some music right now? Use **`%splay`**", Constants.PREFIX) + "**-** Need help? Join the support at **https://discord.gg/HwXHPVK**";

        net.dv8tion.jda.core.EmbedBuilder builder = new net.dv8tion.jda.core.EmbedBuilder()
                .setTitle(title)
                .setColor(Color.CYAN)
                .setFooter("https://nightplay.gq", null)
                .setTimestamp(Instant.now());

        StringBuilder descriptionBuilder = builder.getDescriptionBuilder();

        descriptionBuilder.append(content);

        //TODO: Permission check if the bot can send embed messages
        event.getChannel().sendMessage(builder.build()).queue();

    }







    public void InfoCommand(GuildMessageReceivedEvent event, Guild guild) {

        String title = "Information";


        net.dv8tion.jda.core.EmbedBuilder builder = new net.dv8tion.jda.core.EmbedBuilder()
                .setTitle(title)
                .setColor(Color.CYAN)
                .setFooter("https://nightplay.gq", null)
                .setTimestamp(Instant.now())

                .addField("Shard", String.valueOf(guild.getJDA().getShardInfo().getShardString()), true)
                .addBlankField(true)
                .addField("JDA Version", String.valueOf(jdaInfo), true)
                .addBlankField(true);


        //TODO: Permission check if the bot can send embed messages
        event.getChannel().sendMessage(builder.build()).queue();

    }





    public void StatsCommand(GuildMessageReceivedEvent event, Guild guild) {

        String title = "Status information";



        net.dv8tion.jda.core.EmbedBuilder builder = new net.dv8tion.jda.core.EmbedBuilder()
                .setTitle(title)
                .setColor(Color.BLUE)
                .setFooter("https://nightplay.gq", null)
                .setTimestamp(Instant.now())

                .addField("Guilds:", String.format("`%s` guilds", event.getJDA().asBot().getShardManager().getGuilds().size()), true)
                .addField("Users:", String.format("`%s` users", event.getJDA().asBot().getShardManager().getUsers().size()), true)
                .addField("Latency:", event.getJDA().getPing() + "ms", true)
                .addField("Shards:", String.format("`%s` shards", event.getJDA().asBot().getShardManager().getShards().size()), true)
                .addField("Threads:", String.format("`%s` threads", Thread.getAllStackTraces().size()), true);

              //  .addField("CPU load:", String.format("`%s`)", Math.round(operatingSystemMXBean.getSystemCpuLoad() * 100)) + "%", true)

        //TODO: Permission check if the bot can send embed messages
        event.getChannel().sendMessage(builder.build()).queue();

    }

    public void shardAveragePing(GuildMessageReceivedEvent event) {

        String title = "Average Ping";



        net.dv8tion.jda.core.EmbedBuilder builder = new net.dv8tion.jda.core.EmbedBuilder()
                .setTitle(title)
                .setColor(Color.CYAN)
                .setFooter("https://nightplay.gq", null)
                .setTimestamp(Instant.now())

                .addField("Average Ping:", String.format("`%s` ms", shardManager.getAveragePing()), true);


        //TODO: Permission check if the bot can send embed messages
        event.getChannel().sendMessage(builder.build()).queue();

    }


    public void shardNumberRunning(GuildMessageReceivedEvent event){

        String title = "Number of shards running:";



        net.dv8tion.jda.core.EmbedBuilder builder = new net.dv8tion.jda.core.EmbedBuilder()
                .setTitle(title)
                .setColor(Color.CYAN)
                .setFooter("https://nightplay.gq", null)
                .setTimestamp(Instant.now())

                .addField("Running shards:", String.format("`%s`", shardManager.getShardsRunning()), true);


        //TODO: Permission check if the bot can send embed messages
        event.getChannel().sendMessage(builder.build()).queue();

    }

    public void shardApplicationInfo(GuildMessageReceivedEvent event){

        String title = "Application Info:";



        net.dv8tion.jda.core.EmbedBuilder builder = new net.dv8tion.jda.core.EmbedBuilder()
                .setTitle(title)
                .setColor(Color.CYAN)
                .setFooter("https://nightplay.gq", null)
                .setTimestamp(Instant.now())

                .addField("Application Info:", String.format("`%s`", shardManager.getApplicationInfo()), true);


        //TODO: Permission check if the bot can send embed messages
        event.getChannel().sendMessage(builder.build()).queue();

    }


}
