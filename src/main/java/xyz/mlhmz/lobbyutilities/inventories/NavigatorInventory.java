package xyz.mlhmz.lobbyutilities.inventories;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import xyz.mlhmz.lobbyutilities.utils.BukkitSerialization;
import xyz.mlhmz.lobbyutilities.utils.ChatUtils;

import java.io.IOException;
import java.util.List;

/**
 * singleton navigator inventory
 */
public class NavigatorInventory {
    private static final String[] INVENTORY_ROWS = {
            "abcdefghi",
            "jklmnopqr",
            "       yz"
    };
    private static InventoryGui gui;

    public static InventoryGui get(LobbyUtilities plugin) throws IOException {
        if (gui == null) {
            gui = create(plugin);
        }
        return gui;
    }

    private static InventoryGui create(LobbyUtilities plugin) throws IOException {
        InventoryGui inventoryGui = new InventoryGui(plugin, "&6Navigator", INVENTORY_ROWS);

        createAndSetFiller(inventoryGui);
        createAndAddSpawnElement(plugin, inventoryGui);
        createAndAddServerElement(plugin, inventoryGui);
        createAndAddConfiguredLocations(plugin, inventoryGui);
        return inventoryGui;
    }

    private static void createAndAddConfiguredLocations(LobbyUtilities plugin, InventoryGui inventoryGui) throws IOException {
        // Configurated Locations
        String chars = "abcdefghijklmnopqr";
        List<String> savedLocs = plugin.getConfig().getStringList("savedLocations");

        // to minimize the code a for-each loop with an index is used here instead of a for-index loop
        int index = 0;
        for (int i = 0; i < savedLocs.size(); i++) {
            String locationConfigurationSection = "locations." + savedLocs.get(index);

            ItemStack item =
                    BukkitSerialization.itemStackArrayFromBase64(
                            plugin.getConfig().getString(locationConfigurationSection + ".item")
                    )[0];

            Location l = plugin.getConfig().getLocation(locationConfigurationSection + ".location");

            StaticGuiElement staticGuiElement = new StaticGuiElement(chars.charAt(i),
                    item,
                    1,
                    click -> {
                        assert l != null;
                        click.getWhoClicked().teleport(l);
                        return true;
                    }
            );

            inventoryGui.addElement(staticGuiElement);

            index++;
        }
    }

    private static void createAndAddServerElement(LobbyUtilities plugin, InventoryGui inventoryGui) {
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
                    ServerInventory serverInventory = new ServerInventory(plugin, (Player) click.getWhoClicked());
                    serverInventory.get().show(click.getWhoClicked());
                    return true;
                }
        );

        inventoryGui.addElement(serverGuiElement);
    }

    private static void createAndAddSpawnElement(LobbyUtilities plugin, InventoryGui inventoryGui) {
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
                    click.getWhoClicked().teleport(spawnLocation);
                    return true;
                }
        );

        inventoryGui.addElement(spawnGuiElement);
    }

    private static void createAndSetFiller(InventoryGui inventoryGui) {
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();

        // fallback, should not happen
        assert fillerMeta != null;
        fillerMeta.setDisplayName(ChatUtils.translate("&7/"));

        filler.setItemMeta(fillerMeta);
        inventoryGui.setFiller(filler);
    }
}
