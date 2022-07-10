package xyz.mlhmz.lobbyutilities.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import xyz.mlhmz.lobbyutilities.utils.ChatUtils;

import java.util.logging.Level;
import java.util.logging.Logger;


public class NavigatorItem {
    private static ItemStack item;

    public static ItemStack get(LobbyUtilities plugin) {
        if (item == null) {
            create(plugin);
        }
        return item;
    }

    private static void create(LobbyUtilities plugin) {
        Logger logger = plugin.getLogger();

        String materialIdentifier = plugin.getConfig().getString("navigatoritem.material");

        ItemStack navigatorItem;

        Material material = getMaterialFromConfig(logger, materialIdentifier);

        if (material == null) return;

        // creates the ItemStack out of the material by initializing the variable
        navigatorItem = new ItemStack(material);

        // fallback option, shouldn't happen
        assert navigatorItem.getItemMeta() != null;
        navigatorItem.setItemMeta(getItemMeta(plugin, navigatorItem.getItemMeta()));

        // sets the navigator item
        item = navigatorItem;
    }

    /**
     * gets material from config
     *
     * @return the material from the config, returns feather if material from config is invalid
     */
    private static Material getMaterialFromConfig(Logger logger, String materialIdentifier) {
        if (materialIdentifier == null || materialIdentifier.isEmpty()) {
            logger.log(Level.SEVERE, "The navigator is not configured yet... " +
                    "Falling back to the default Option");
            return null;
        }


        Material material = Material.getMaterial(materialIdentifier);

        if (material == null) {
            logger.log(Level.SEVERE, "The configured navigator item is invalid... " +
                    "Falling back to the default Option");
            material = Material.FEATHER;
        }
        return material;
    }

    /**
     * creates the meta of the navigator item
     *
     * @return the item meta
     */
    private static ItemMeta getItemMeta(LobbyUtilities plugin, ItemMeta itemMeta) {
        assert itemMeta != null;
        itemMeta.setDisplayName(ChatUtils.translate(plugin.getConfig().getString("navigatoritem.name")));
        return itemMeta;
    }

}
