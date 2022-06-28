package xyz.mlhmz.lobbyutilities.commands;

import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import xyz.mlhmz.lobbyutilities.utils.ChatUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    private LobbyUtilities plugin;

    public SpawnCommand(LobbyUtilities plugin) {
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
            p.sendMessage(LobbyUtilities.prefix + ChatUtils.translate("&cEs wurde kein Spawn gesetzt!"));
        }


        Location spawn = plugin.getConfig().getLocation("spawn");
        p.teleport(spawn);
        p.getInventory().clear();
        p.getInventory().setItem(0, LobbyUtilities.items.getNavigatorItem());

        return true;
    }
}
