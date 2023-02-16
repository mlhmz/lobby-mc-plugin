package xyz.mlhmz.lobbyutilities.command;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import xyz.mlhmz.lobbyutilities.item.NavigatorItem;
import xyz.mlhmz.lobbyutilities.util.ChatUtils;

public class BuildCommand extends ExtendedCommand {
    private final LobbyUtilities plugin;

    public BuildCommand(LobbyUtilities plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player p)) {
            sender.sendMessage("Nur Spieler können diesen Befehl ausführen!");
            return true;
        }

        Location lobbyLocation = plugin.getConfig().getLocation("spawn");

        // Check if user is permitted and in the right world to build
        if (!p.hasPermission("lobby.build")) {
            p.sendMessage(LobbyUtilities.prefix + ChatUtils.translate("&cDu hast keine Rechte dazu!"));
            return true;
        }
        if (lobbyLocation == null) {
            p.sendMessage(LobbyUtilities.prefix + ChatUtils.translate("&cDie Lobbywelt wurde noch nicht eingerichtet"));
            return true;
        }
        if (!(p.getWorld().equals(lobbyLocation.getWorld()))) {
            p.sendMessage(LobbyUtilities.prefix + "Du bist nicht in der Lobbywelt.");
            return true;
        }

        if(LobbyUtilities.builderList.contains(p.getUniqueId())) {
            p.setGameMode(GameMode.SURVIVAL);
            p.sendMessage(LobbyUtilities.prefix + ChatUtils.translate("Du hast den &2Buildmodus &causgeschaltet&7!"));
            LobbyUtilities.builderList.remove(p.getUniqueId());
            p.getInventory().clear();
            p.getInventory().setItem(0, NavigatorItem.get(plugin));
        } else {
            p.setGameMode(GameMode.CREATIVE);
            p.sendMessage(LobbyUtilities.prefix + ChatUtils.translate("Du hast den &2Buildmodus &aangeschaltet&7!"));
            LobbyUtilities.builderList.add(p.getUniqueId());
            p.getInventory().clear();
        }
        return true;
    }

    @Override
    public String getIdentifier() {
        return "build";
    }
}
