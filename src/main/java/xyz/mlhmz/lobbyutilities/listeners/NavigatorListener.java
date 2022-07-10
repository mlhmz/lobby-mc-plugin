package xyz.mlhmz.lobbyutilities.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import xyz.mlhmz.lobbyutilities.inventories.NavigatorInventory;

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

        if (spawn == null) return;

        if (!(p.getWorld() == spawn.getWorld())) return;

        if (LobbyUtilities.builderList.contains(p.getUniqueId())) return;

        if (p.getInventory().getItemInMainHand().getType() != Material.FEATHER) return;

        NavigatorInventory.get(plugin).show(p);
    }


}
