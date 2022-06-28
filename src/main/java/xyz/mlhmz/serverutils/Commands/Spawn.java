package xyz.mlhmz.serverutils.Commands;

import xyz.mlhmz.serverutils.Serverutils;
import xyz.mlhmz.serverutils.Utils.ChatUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {
    private Serverutils plugin;

    public Spawn(Serverutils plugin) {
        this.plugin = plugin;

        plugin.getCommand("spawn").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl ausführen!");
            return true;
        }

        Player p = (Player) sender;

        if (plugin.getConfig().getLocation("spawn") == null) {
            p.sendMessage(Serverutils.prefix + ChatUtils.translate("&cEs wurde kein Spawn gesetzt!"));
        }


        Location spawn = plugin.getConfig().getLocation("spawn");
        p.teleport(spawn);
        p.getInventory().clear();
        p.getInventory().setItem(0, Serverutils.items.getNavigatorItem());

        return true;
    }
}
