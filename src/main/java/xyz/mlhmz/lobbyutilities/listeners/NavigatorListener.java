package xyz.mlhmz.lobbyutilities.listeners;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import xyz.mlhmz.lobbyutilities.inventories.ServerInventory;
import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import xyz.mlhmz.lobbyutilities.utils.BukkitSerialization;
import xyz.mlhmz.lobbyutilities.utils.ChatUtils;
import xyz.mlhmz.lobbyutilities.objects.NavigationEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.List;

public class NavigatorListener implements Listener {
    private LobbyUtilities plugin;

    public NavigatorListener(LobbyUtilities plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getWorld() == plugin.getConfig().getLocation("spawn").getWorld()) {
            if (!LobbyUtilities.builderlist.contains(p.getUniqueId())) {
                if (p.getInventory().getItemInMainHand().getType() == Material.FEATHER) {
                    String[] rows = {
                        "abcdefghi",
                        "jklmnopqr",
                        "       yz"
                    };

                    InventoryGui inventoryGui = new InventoryGui(plugin, "&6Navigator", rows);

                    // Filler
                    ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                    ItemMeta fillerMeta = filler.getItemMeta();
                    fillerMeta.setDisplayName(ChatUtils.translate("&7/"));
                    filler.setItemMeta(fillerMeta);
                    inventoryGui.setFiller(filler);


                    // Spawn Location
                    Location spawnLocation = plugin.getConfig().getLocation("spawn");

                    ItemStack spawnItem = new ItemStack(Material.SLIME_BALL);
                    ItemMeta itemMeta = spawnItem.getItemMeta();
                    itemMeta.setDisplayName(ChatUtils.translate("&6Spawn"));
                    spawnItem.setItemMeta(itemMeta);

                    StaticGuiElement spawnGuiElement = new StaticGuiElement('z',
                            spawnItem,
                            1,
                            click -> {
                                assert spawnLocation != null;
                                p.teleport(spawnLocation);
                                return true;
                            }
                    );

                    inventoryGui.addElement(spawnGuiElement);

                    // Servers will be added with the Configuration
                    ItemStack serverItem = new ItemStack(Material.EMERALD);
                    ItemMeta serverMeta = serverItem.getItemMeta();
                    serverMeta.setDisplayName(ChatUtils.translate("&6Servers"));
                    serverItem.setItemMeta(serverMeta);

                    StaticGuiElement serverGuiElement = new StaticGuiElement('y',
                            serverItem,
                            1,
                            click -> {
                                ServerInventory serverInventory = new ServerInventory(plugin, p);
                                serverInventory.get().show(p);
                                return true;
                            }
                    );

                    inventoryGui.addElement(serverGuiElement);





                    // Configurated Locations
                    String chars = "abcdefghijklmnopqr";
                    List<String> savedLocs = plugin.getConfig().getStringList("savedLocations");
                    for (int i = 0; i < savedLocs.size(); i++) {
                        String locationConfigurationSection = "locations." + savedLocs.get(i);

                        // Name
                        NavigationEntry navigationEntry = new NavigationEntry();
                        navigationEntry.setName(plugin.getConfig().getString(locationConfigurationSection + ".name"));

                        // ItemStack
                        ItemStack item = null;
                        try {
                            item = BukkitSerialization.itemStackArrayFromBase64(plugin.getConfig().getString(locationConfigurationSection + ".item"))[0];
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        // Location
                        Location l = plugin.getConfig().getLocation(locationConfigurationSection + ".location");

                        char c = chars.charAt(i);
                        StaticGuiElement staticGuiElement = new StaticGuiElement(c,
                                    item,
                                    1,
                                    click -> {
                                        assert l != null;
                                        p.teleport(l);
                                        return true;
                                    }
                                    );

                        inventoryGui.addElement(staticGuiElement);


                    }

                    inventoryGui.show(p);
                }
            }
        }
    }
}
