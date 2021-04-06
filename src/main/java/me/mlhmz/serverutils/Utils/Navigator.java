package me.mlhmz.serverutils.Utils;

import me.mlhmz.serverutils.Serverutils;
import me.mlhmz.serverutils.objects.NavEntry;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class Navigator {
    private Serverutils plugin;

    public Navigator(Serverutils plugin) { this.plugin = plugin; }

    public void create(NavEntry entry) {
        // Name in List
        Configuration config = plugin.getConfig();
        config.getStringList("savedLocations").add(entry.getName());

        // New Section & Location
        ConfigurationSection section = config.getConfigurationSection("locations").createSection(entry.getName());
        section.addDefault("location", entry.getLocation());


        // ItemStack to Base64
        ItemStack[] itemArr = {entry.getItem()};
        section.addDefault("item", BukkitSerialization.itemStackArrayToBase64(itemArr));
    }
}
