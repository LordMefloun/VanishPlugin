package me.lordmefloun.vanish;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.swing.*;
import java.util.Map;
import java.util.UUID;

public class ActionbarRunnable {



    Vanish plugin;

    public ActionbarRunnable(Vanish plugin){
        this.plugin = plugin;
    }

    public void actionbar(){
        new BukkitRunnable(){

            @Override
            public void run(){
                for (Map.Entry<UUID, Boolean> entry : plugin.vanishedPlayers.entrySet()) {
                    if(entry.getValue()) {

                        Player p = Bukkit.getPlayer(entry.getKey());

                        p.sendActionBar(ChatColor.translateAlternateColorCodes('&',  plugin.getConfig().getString("actionbar")));

                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}
