package net.cloudybyte.bot.util;

import net.cloudybyte.bot.core.Constants;
import net.cloudybyte.bot.core.data.MySQLManager;
import net.dv8tion.jda.api.entities.Guild;


public class GuildVolumeHandler {
    MySQLManager mySQLManager = new MySQLManager(Constants.DBHost, Constants.DBport, Constants.DBUser, Constants.DBpw, Constants.DBName);

    public void setVolume(Guild guild, Integer volume) throws MySQLManager.MySQL_NotConnectedQueryException {
        mySQLManager.connect();
        long guildid = guild.getIdLong();
        mySQLManager.insertandupdate("volumes", new String[]{"guildid", "volume"},new Object[]{guildid, volume}, "volume", volume);
        System.out.println("2");
        mySQLManager.disconnect();
    }

}