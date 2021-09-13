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






    public HashMap<UUID, Boolean> vanishedPlayers = new HashMap<>();

    public MySql mysql = new MySql(this);

    @Override
    public void onEnable() {
        getCommand("vanish").setExecutor(new VanishCommand(this));
        getServer().getPluginManager().registerEvents(new Events(this), this);
        saveDefaultConfig();


        mysql.mysqlSetup();
        ActionbarRunnable actionbar = new ActionbarRunnable(this);

        actionbar.actionbar();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public void updateVanishPlayers(){

        for (Map.Entry<UUID, Boolean> entry : vanishedPlayers.entrySet()) {
            if(entry.getValue()) {

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


}
