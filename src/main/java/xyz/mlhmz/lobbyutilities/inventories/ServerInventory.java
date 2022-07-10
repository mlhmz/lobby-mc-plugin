package xyz.mlhmz.lobbyutilities.inventories;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import xyz.mlhmz.lobbyutilities.utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerInventory {
    private LobbyUtilities plugin;
    private Player player;

    public ServerInventory(LobbyUtilities plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public InventoryGui get() {
        String[] rows = {
            "abcdefghi"
        };
        InventoryGui gui = new InventoryGui(plugin, ChatUtils.translate("&6Servers"), rows);

        int index = 0;
        for (String server : plugin.getConfig().getStringList("servers")) {
            ItemStack item = new ItemStack(Material.ENDER_PEARL);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatUtils.translate("&6" + server));
            item.setItemMeta(meta);

            StaticGuiElement guiElement = new StaticGuiElement(
                    rows[0].charAt(index),
                    item,
                    click -> {
                        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            DataOutputStream dos = new DataOutputStream(baos)) {
                            player.sendMessage("Connecting to server " + server + "...");
                            dos.writeUTF("Connect");
                            dos.writeUTF(server);
                            player.sendPluginMessage(plugin, "BungeeCord", baos.toByteArray());
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        return true;
                    }
                    );

            gui.addElement(guiElement);
            index++;

        }

        return gui;
    }
}
