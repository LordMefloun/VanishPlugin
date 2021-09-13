package me.lordmefloun.vanish;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utils {
    public static void sendMessage(Player p, String message){
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

}
