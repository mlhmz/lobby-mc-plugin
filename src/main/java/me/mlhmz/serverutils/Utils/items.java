package me.mlhmz.serverutils.Utils;

import de.themoep.inventorygui.InventoryGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class items {
    public static ItemStack feather = new ItemStack(Material.FEATHER);
    public static ItemMeta feathermeta = feather.getItemMeta();

    public static void givefeather(Player p, int slot) {
        assert feathermeta != null;
        feathermeta.setDisplayName("§2navigation §7x §8rechtsklick");
        feather.setItemMeta(feathermeta);
        p.getInventory().setItem(slot, feather);
    }


}
