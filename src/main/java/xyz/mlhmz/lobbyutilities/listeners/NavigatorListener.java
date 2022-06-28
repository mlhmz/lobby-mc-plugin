package xyz.mlhmz.lobbyutilities.listeners;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import xyz.mlhmz.lobbyutilities.inventories.ServerInventory;
import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import xyz.mlhmz.lobbyutilities.utils.BukkitSerialization;
import xyz.mlhmz.lobbyutilities.utils.ChatUtils;
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
    private static final String[] INVENTORY_ROWS = {
            "abcdefghi",
            "jklmnopqr",
            "       yz"
    };
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

        InventoryGui inventoryGui = new InventoryGui(plugin, "&6Navigator", INVENTORY_ROWS);

        createAndSetFiller(inventoryGui);
        createAndAddSpawnElement(p, inventoryGui);
        createAndAddServerElement(p, inventoryGui);
        createAndAddConfiguredLocations(p, inventoryGui);

        inventoryGui.show(p);
    }

    private void createAndAddConfiguredLocations(Player p, InventoryGui inventoryGui) throws IOException {
        // Configurated Locations
        String chars = "abcdefghijklmnopqr";
        List<String> savedLocs = plugin.getConfig().getStringList("savedLocations");

        // to minimize the code a for-each loop with an index is used here instead of a for-index loop
        int index = 0;
        for (char character : chars.toCharArray()) {
            String locationConfigurationSection = "locations." + savedLocs.get(index);

            ItemStack item =
                    BukkitSerialization.itemStackArrayFromBase64(
                            plugin.getConfig().getString(locationConfigurationSection + ".item")
                    )[0];

            Location l = plugin.getConfig().getLocation(locationConfigurationSection + ".location");

            StaticGuiElement staticGuiElement = new StaticGuiElement(character,
                    item,
                    1,
                    click -> {
                        assert l != null;
                        p.teleport(l);
                        return true;
                    }
            );

            inventoryGui.addElement(staticGuiElement);

            index++;
        }
    }

    private void createAndAddServerElement(Player p, InventoryGui inventoryGui) {
        ItemStack serverItem = new ItemStack(Material.EMERALD);
        ItemMeta serverMeta = serverItem.getItemMeta();

        // fallback asserting, should not happen
        assert serverMeta != null;
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
    }

    private void createAndAddSpawnElement(Player p, InventoryGui inventoryGui) {
        Location spawnLocation = plugin.getConfig().getLocation("spawn");

        ItemStack spawnItem = new ItemStack(Material.SLIME_BALL);
        ItemMeta itemMeta = spawnItem.getItemMeta();

        // fallback, should not happen
        assert itemMeta != null;
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
    }

    private void createAndSetFiller(InventoryGui inventoryGui) {
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();

        // fallback, should not happen
        assert fillerMeta != null;
        fillerMeta.setDisplayName(ChatUtils.translate("&7/"));

        filler.setItemMeta(fillerMeta);
        inventoryGui.setFiller(filler);
    }
}
