package me.lordmefloun.vanish;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Vanish extends JavaPlugin {

    private Connection connection;
    public String host, database, username, password, table;
    public String offmessage;
    public String onmessage;
    public int port;
    public HashMap<UUID, Boolean> vanishedPlayers = new HashMap<>();

    @Override
    public void onEnable() {
        getCommand("vanish").setExecutor(new VanishCommand(this));
        getServer().getPluginManager().registerEvents(new Events(), this);
        saveDefaultConfig();

        offmessage = getConfig().getString("messages.off");
        onmessage = getConfig().getString("messages.on");

        mysqlSetup();
        actionbar();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void mysqlSetup() {
        host = getConfig().getString("database.host");
        port = getConfig().getInt("database.port");
        database = getConfig().getString("database.database");
        username = getConfig().getString("database.user");
        password = getConfig().getString("database.password");
        table = getConfig().getString("database.table");
        
        
        try{
            synchronized (this){
                if (getConnection() != null && !getConnection().isClosed() ) {
                    return;
                }

                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" +
                        this.port + "/" + this.database, this.username, this.password));
                Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "MySQL connection successfull");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        try {
            PreparedStatement statement = this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `"+ database +"`.`" + table + "` ( `id` INT(225) NOT NULL AUTO_INCREMENT , `player` VARCHAR(225) NOT NULL , `vanished` BOOLEAN NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;");
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateVanishPlayers(){

        for (Map.Entry<UUID, Boolean> entry : vanishedPlayers.entrySet()) {
            if(entry.getValue() == true) {

                Player p = Bukkit.getPlayer(entry.getKey());

                for (Player playerToHide : Bukkit.getOnlinePlayers()) {
                    if (playerToHide != p) {
                        playerToHide.hidePlayer(p);
                    }
                }
            }else{

                Player p = Bukkit.getPlayer(entry.getKey());

                for (Player playerToHide : Bukkit.getOnlinePlayers()) {
                    if (playerToHide != p) {
                        playerToHide.showPlayer(p);
                    }
                }

            }
        }

    }



    public void actionbar(){
        new BukkitRunnable(){

            @Override
            public void run(){
                for (Map.Entry<UUID, Boolean> entry : vanishedPlayers.entrySet()) {
                    if(entry.getValue() == true) {

                        Player p = Bukkit.getPlayer(entry.getKey());

                        p.sendActionBar(ChatColor.translateAlternateColorCodes('&',  getConfig().getString("actionbar")));

                    }
                }
            }
        }.runTaskTimer(this, 0, 20);
    }


    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
