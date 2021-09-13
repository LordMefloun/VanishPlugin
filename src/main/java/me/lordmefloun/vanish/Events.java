package me.lordmefloun.vanish;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {


    Vanish plugin;

    public Events (Vanish plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){


        MySql mysql = plugin.mysql;

        Player p = e.getPlayer();

        if (plugin.vanishedPlayers.get(p.getUniqueId()) == null){

            plugin.vanishedPlayers.put(p.getUniqueId(), false);

            if(!mysql.vanishStateExists(p.getUniqueId())){
                mysql.createVanishState(p.getUniqueId(), false);
            }
            plugin.vanishedPlayers.put(p.getUniqueId(), mysql.getVanishState(p.getUniqueId()));
        }

        plugin.updateVanishPlayers();

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        MySql mysql = plugin.mysql;

        Player p = e.getPlayer();


        mysql.setVanishState(p.getUniqueId() ,plugin.vanishedPlayers.get(p.getUniqueId()));
        plugin.vanishedPlayers.remove(p.getUniqueId());
    }

}
