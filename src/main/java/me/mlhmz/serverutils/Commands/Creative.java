package me.mlhmz.serverutils.Commands;

import me.mlhmz.serverutils.Serverutils;
import me.mlhmz.serverutils.Utils.Items;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Creative implements CommandExecutor {
    private Serverutils plugin;

    public Creative(Serverutils plugin) {
        this.plugin = plugin;

        plugin.getCommand("build").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl ausführen!");
            return true;
        }
        Player p = (Player) sender;
        if(p.hasPermission("lobby.build")) {
            if (p.getWorld() == plugin.getConfig().getLocation("spawn").getWorld()) {
                if(!Serverutils.builderlist.contains(p.getUniqueId())) {
                    p.setGameMode(GameMode.CREATIVE);
                    p.sendMessage(Serverutils.prefix + "Du hast den §2Buildmodus §aangeschaltet§7!");
                    Serverutils.builderlist.add(p.getUniqueId());
                    p.getInventory().clear();
                } else {

                    p.setGameMode(GameMode.SURVIVAL);
                    p.sendMessage(Serverutils.prefix + "Du hast den §2Buildmodus §causgeschaltet§7!");
                    Serverutils.builderlist.remove(p.getUniqueId());
                    p.getInventory().clear();
                    p.getInventory().setItem(0, Serverutils.items.getNavigatorItem());
                    ;
                }
            } else {
                p.sendMessage(Serverutils.prefix + "Du bist nicht in der Lobbywelt.");
            }
            return true;
        } else {
            p.sendMessage(Serverutils.prefix + "§cDu hast keine Rechte dazu!");
        }
        return false;
    }
}
