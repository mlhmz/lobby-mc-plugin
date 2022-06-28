package xyz.mlhmz.lobbyutilities.utils;

import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import xyz.mlhmz.lobbyutilities.objects.NavigationEntry;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Navigator {
    private LobbyUtilities plugin;

    public Navigator(LobbyUtilities plugin) { this.plugin = plugin; }

    public boolean create(NavigationEntry entry) {
        // Name in List
        List<String> locList = plugin.getConfig().getStringList("savedLocations");
        if (locList.contains(entry.getName())) {
            return false;
        }

        locList.add(entry.getName());
        plugin.getConfig().set("savedLocations", locList);

        // New Section & Location
        plugin.getConfig().set("locations." + entry.getName() + ".location", entry.getLocation());


        // ItemStack to Base64
        ItemStack[] itemArr = {entry.getItem()};
        plugin.getConfig().set("locations." + entry.getName() + ".item", BukkitSerialization.itemStackArrayToBase64(itemArr));

        plugin.saveConfig();

        return true;
    }

    public boolean delete(String name) {
        // Name in List
        List<String> locList = plugin.getConfig().getStringList("savedLocations");

        if (!locList.contains(name)) {
            return false;
        }

        locList.remove(name);
        plugin.getConfig().set("savedLocations", locList);

        plugin.getConfig().set("locations." + name, null);

        plugin.saveConfig();

        return true;
    }
}
