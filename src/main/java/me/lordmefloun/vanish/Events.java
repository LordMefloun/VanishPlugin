package me.lordmefloun.vanish;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {


    Vanish plugin = Vanish.getPlugin(Vanish.class);

    @EventHandler
    public void onJoin(PlayerJoinEvent e){


        mysql mysql = new mysql();

        Player p = e.getPlayer();

        if (plugin.vanishedPlayers.get(p.getUniqueId()) == null){

            plugin.vanishedPlayers.put(p.getUniqueId(), false);

            if(!mysql.VanishStateExists(p.getUniqueId())){
                mysql.createVanishState(p.getUniqueId(), false);
            }
            plugin.vanishedPlayers.put(p.getUniqueId(), mysql.getVanishState(p.getUniqueId()));
        }

        plugin.updateVanishPlayers();

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        mysql mysql = new mysql();

        Player p = e.getPlayer();


        mysql.setVanishState(p.getUniqueId() ,plugin.vanishedPlayers.get(p.getUniqueId()));
    }

}
