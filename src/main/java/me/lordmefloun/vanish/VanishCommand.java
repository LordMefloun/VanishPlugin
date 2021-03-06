package me.lordmefloun.vanish;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class VanishCommand implements CommandExecutor {

    Vanish plugin;

    public VanishCommand (Vanish plugin){
        this.plugin = plugin;
    }





    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }

        Player p = (Player) sender;

        if (args.length == 1)
        {
            if (p.hasPermission("vanish.other")){

                Player player = Bukkit.getPlayer(args[0]);
                vanishPlayer(player);
                plugin.updateVanishPlayers();
                Utils.sendMessage(p, "&aZměnil jsi vanish mod hrači &6" + player.getName());
            }
            else{
                Utils.sendMessage(p, "&cNemáš na tohle pravomoce");
            }
        }

        if (args.length == 0){
            if (p.hasPermission("vanish.use")){
                vanishPlayer(p);
                plugin.updateVanishPlayers();
            }
        }

        if (args.length > 1){
            Utils.sendMessage(p, "&cNeplatné použití příkazu");
        }

        return false;
    }


    public void vanishPlayer(Player p){

        MySql mysql = plugin.mysql;

        if (plugin.vanishedPlayers.get(p.getUniqueId()) != null) {
            if (plugin.vanishedPlayers.get(p.getUniqueId())) {

                plugin.vanishedPlayers.put(p.getUniqueId(), false);

            } else {
                plugin.vanishedPlayers.put(p.getUniqueId(), true);
            }


            if (plugin.vanishedPlayers.get(p.getUniqueId())){
                Utils.sendMessage(p, plugin.getConfig().getString("offMessage"));
            }
            else{
                Utils.sendMessage(p, plugin.getConfig().getString("onMessage"));
            }

        }else{
            plugin.vanishedPlayers.put(p.getUniqueId(), mysql.getVanishState(p.getUniqueId()));


            if (plugin.vanishedPlayers.get(p.getUniqueId())) {

                plugin.vanishedPlayers.put(p.getUniqueId(), false);

            } else {
                plugin.vanishedPlayers.put(p.getUniqueId(), true);
            }

            if (plugin.vanishedPlayers.get(p.getUniqueId())){
                Utils.sendMessage(p, plugin.getConfig().getString("offMessage"));
            }
            else{
                Utils.sendMessage(p, plugin.getConfig().getString("onMessage"));
            }
        }
    }
}
