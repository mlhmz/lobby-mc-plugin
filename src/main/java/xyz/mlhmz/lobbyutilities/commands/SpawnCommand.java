package xyz.mlhmz.lobbyutilities.commands;

import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import xyz.mlhmz.lobbyutilities.utils.ChatUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SpawnCommand implements CommandExecutor {
    private final LobbyUtilities plugin;

    public SpawnCommand(LobbyUtilities plugin) {
        this.plugin = plugin;

        Objects.requireNonNull(plugin.getCommand("spawn")).setExecutor(this);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Nur Spieler können diesen Befehl ausführen!");
            return true;
        }

        Location spawn = plugin.getConfig().getLocation("spawn");

        if (Objects.isNull(spawn)) {
            p.sendMessage(LobbyUtilities.prefix + ChatUtils.translate("&cEs wurde kein Spawn gesetzt!"));
            return true;
        }

        p.teleport(spawn);
        p.getInventory().clear();
        p.getInventory().setItem(0, LobbyUtilities.items.getNavigatorItem());

        return true;
    }
}
