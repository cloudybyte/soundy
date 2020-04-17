package net.cloudybyte.bot.util;

import com.sun.management.OperatingSystemMXBean;
import com.sun.org.apache.bcel.internal.Const;
import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.data.MySQLManager;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.time.Instant;

public class EmbedBuilder{

    private static final String URL = Constants.URL;
    private static final String iconURL = Constants.iconURL;
    public JDAInfo jdaInfo = new JDAInfo();
    private ShardManager shardManager;
    private OperatingSystemMXBean operatingSystemMXBean;
    public JDA jda;
    MySQLManager mySQLManager = new MySQLManager("localhost", "3306", "root", "", "soundy");










    public void info(GuildMessageReceivedEvent event, String title, String content) {

        net.dv8tion.jda.api.EmbedBuilder builder = new net.dv8tion.jda.api.EmbedBuilder()
                .setTitle(title)
                .setColor(Color.white)
                .setFooter(URL, iconURL)
                .setTimestamp(Instant.now());

        StringBuilder descriptionBuilder = builder.getDescriptionBuilder();

        descriptionBuilder.append(content);

        //TODO: Permission check if the bot can send embed messages
        event.getChannel().sendMessage(builder.build()).queue();

    }


    public void success(GuildMessageReceivedEvent event, String title, String content) {

        net.dv8tion.jda.api.EmbedBuilder builder = new net.dv8tion.jda.api.EmbedBuilder()
                .setTitle(title)
                .setColor(Color.green)
                .setFooter(URL, iconURL)
                .setTimestamp(Instant.now());

        StringBuilder descriptionBuilder = builder.getDescriptionBuilder();

        descriptionBuilder.append(content);

        //TODO: Permission check if the bot can send embed messages
        event.getChannel().sendMessage(builder.build()).queue();

    }

    public void warn(GuildMessageReceivedEvent event, String title, String content) {

        net.dv8tion.jda.api.EmbedBuilder builder = new net.dv8tion.jda.api.EmbedBuilder()
                .setTitle(title)
                .setColor(Color.orange)
                .setFooter(URL, iconURL)
                .setTimestamp(Instant.now());

        StringBuilder descriptionBuilder = builder.getDescriptionBuilder();

        descriptionBuilder.append(content);

        //TODO: Permission check if the bot can send embed messages
        event.getChannel().sendMessage(builder.build()).queue();

    }

    public void error(GuildMessageReceivedEvent event, String title, String content) {

        net.dv8tion.jda.api.EmbedBuilder builder = new net.dv8tion.jda.api.EmbedBuilder()
                .setTitle(title)
                .setColor(Color.red)
                .setFooter(URL, iconURL)
                .setTimestamp(Instant.now());

        StringBuilder descriptionBuilder = builder.getDescriptionBuilder();

        descriptionBuilder.append(content);

        //TODO: Permission check if the bot can send embed messages
        event.getChannel().sendMessage(builder.build()).queue();

    }

    public void welcome(GuildMessageReceivedEvent event, Guild guild) {

        String title = String.format("**Hey!** My name is %s and I am the best music bot on Discord! **Seriously!**", guild.getJDA().getSelfUser().getName());
        String content = String.format("**-** My prefix is: `" + Constants.PREFIX + "`") + String.format("**-** I got many commands! Request a list by typing: `" + Constants.PREFIX + "help`") + String.format("**-** You wanna start listening to some music right now? Use **`%splay`**", Constants.PREFIX) + "**-** Need help? Join the support at **https://discord.gg/HwXHPVK**";

        net.dv8tion.jda.api.EmbedBuilder builder = new net.dv8tion.jda.api.EmbedBuilder()
                .setTitle(title)
                .setColor(Color.CYAN)
                .setFooter(URL, iconURL)
                .setTimestamp(Instant.now());

        StringBuilder descriptionBuilder = builder.getDescriptionBuilder();

        descriptionBuilder.append(content);

        //TODO: Permission check if the bot can send embed messages
        event.getChannel().sendMessage(builder.build()).queue();

    }







    public void InfoCommand(GuildMessageReceivedEvent event, Guild guild) {

        String title = "Information";


        net.dv8tion.jda.api.EmbedBuilder builder = new net.dv8tion.jda.api.EmbedBuilder()
                .setTitle(title)
                .setColor(Color.CYAN)
                .setFooter(URL, iconURL)
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

        mySQLManager.connect();

        net.dv8tion.jda.api.EmbedBuilder builder = new net.dv8tion.jda.api.EmbedBuilder()
                .setTitle(title)
                .setColor(Color.BLUE)
                .setFooter(URL, iconURL)
                .setTimestamp(Instant.now())

                .addField("Guilds:", String.format("`%s` guilds", event.getJDA().getShardManager().getGuilds().size()), true)
                .addField("Users:", String.format("`%s` users", event.getJDA().getShardManager().getUsers().size()), true)
                .addField("Shards:", String.format("`%s` shards", event.getJDA().getShardManager().getShards().size()), true)
              //  .addField("DB ping:", String.format("`%sms`", mySQLManager.query("/* ping */ SELECT 1") ), true)
                .addField("Threads:", String.format("`%s` threads", Thread.getAllStackTraces().size()), true);

              //  .addField("CPU load:", String.format("`%s`)", Math.round(operatingSystemMXBean.getSystemCpuLoad() * 100)) + "%", true)

        //TODO: Permission check if the bot can send embed messages
        event.getChannel().sendMessage(builder.build()).queue();

    }

    public void shardAveragePing(GuildMessageReceivedEvent event) {

        String title = "Average Ping";



        net.dv8tion.jda.api.EmbedBuilder builder = new net.dv8tion.jda.api.EmbedBuilder()
                .setTitle(title)
                .setColor(Color.CYAN)
                .setFooter(URL, iconURL)
                .setTimestamp(Instant.now())

                .addField("Average Ping:", String.format("`%s` ms", shardManager.getAverageGatewayPing()), true);


        //TODO: Permission check if the bot can send embed messages
        event.getChannel().sendMessage(builder.build()).queue();

    }


        public void shardNumberRunning(GuildMessageReceivedEvent event){

            String title = "Number of shards running:";



            net.dv8tion.jda.api.EmbedBuilder builder = new net.dv8tion.jda.api.EmbedBuilder()
                    .setTitle(title)
                    .setColor(Color.CYAN)
                    .setFooter(URL, iconURL)
                    .setTimestamp(Instant.now())

                    .addField("Running shards:", String.format("`%s`", shardManager.getShardsRunning()), true);


            //TODO: Permission check if the bot can send embed messages
            event.getChannel().sendMessage(builder.build()).queue();

        }

    public void shardApplicationInfo(GuildMessageReceivedEvent event){

        String title = "Application Info:";



        net.dv8tion.jda.api.EmbedBuilder builder = new net.dv8tion.jda.api.EmbedBuilder()
                .setTitle(title)
                .setColor(Color.CYAN)
                .setFooter(URL, iconURL)
                .setTimestamp(Instant.now())

                .addField("Application Info:", String.format("`%s`", JDAInfo.VERSION), true);


        //TODO: Permission check if the bot can send embed messages
        event.getChannel().sendMessage(builder.build()).queue();

    }


    public void nowPlayingCommand(GuildMessageReceivedEvent event, String songname) {

        String title = "Now playing";


        net.dv8tion.jda.api.EmbedBuilder builder = new net.dv8tion.jda.api.EmbedBuilder()
                .setColor(Color.BLUE)
                .setFooter(URL, iconURL)
                .addField("queued:", Reactions.PLAY + " `" + songname + "`", false);

        //TODO: Permission check if the bot can send embed messages
        event.getChannel().sendMessage(builder.build()).queue();

    }


}
