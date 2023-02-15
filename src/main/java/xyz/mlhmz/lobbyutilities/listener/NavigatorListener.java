package xyz.mlhmz.lobbyutilities.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import xyz.mlhmz.lobbyutilities.inventory.NavigatorInventory;

import java.io.IOException;

/**
 * listener for the navigator lobby item
 * opens on interaction the navigator inventory gui
 */
public class NavigatorListener implements Listener {

    private final LobbyUtilities plugin;

    public NavigatorListener(LobbyUtilities plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void interact(PlayerInteractEvent e) throws IOException {
        Player p = e.getPlayer();
        Location spawn = plugin.getConfig().getLocation("spawn");

        if (isPlayerInSpawnWorldAndNotInBuilderList(p, spawn) && isPlayerHoldingFeather(p)) {
            NavigatorInventory.get(plugin).show(p);
        }
    }

    private boolean isPlayerInSpawnWorldAndNotInBuilderList(Player p, Location spawn) {
        return isPlayerInSpawnWorld(p, spawn) && isBuilderListNotContainingPlayer(p);
    }

    private static boolean isPlayerInSpawnWorld(Player p, Location spawn) {
        return isSpawnNotNull(spawn) && p.getWorld() == spawn.getWorld();
    }

    private static boolean isSpawnNotNull(Location spawn) {
        return spawn != null;
    }

    private boolean isBuilderListNotContainingPlayer(Player p) {
        return !LobbyUtilities.builderList.contains(p.getUniqueId());
    }

    private boolean isPlayerHoldingFeather(Player p) {
        return p.getInventory().getItemInMainHand().getType().equals(Material.FEATHER);
    }
}
