package xyz.mlhmz.lobbyutilities.objects;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class NavigationEntry {
    private String name;
    private ItemStack item;
    private Location location;

    public NavigationEntry(String name, ItemStack item, Location location) {
        this.name = name;
        this.item = item;
        this.location = location;
    }

    public NavigationEntry(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public NavigationEntry(String name) {
        this.name = name;
    }

    public NavigationEntry() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}