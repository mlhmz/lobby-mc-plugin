package xyz.mlhmz.lobbyutilities.inventory;

import de.themoep.inventorygui.GuiElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import xyz.mlhmz.lobbyutilities.util.ChatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerInventory {
    private static final String[] INVENTORY_ROWS = {
            "abcdefghi"
    };
    
    private final LobbyUtilities plugin;
    private final Player player;

    public ServerInventory(LobbyUtilities plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public InventoryGui get() {
        InventoryGui gui = new InventoryGui(plugin, ChatUtils.translate("&6Servers"), INVENTORY_ROWS);
        int index = 0;
        for (String server : plugin.getConfig().getStringList("servers")) {
            getServerGuiElement(gui, index, server);
            index++;
        }
        return null;
    }

    private void getServerGuiElement(InventoryGui gui, int index, String server) {
        ItemStack item = getServerIconWithName(server);
        gui.addElement(
                new StaticGuiElement(INVENTORY_ROWS[0].charAt(index), item, getInventoryServerAction(server))
        );
    }

    private static ItemStack getServerIconWithName(String server) {
        ItemStack item = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatUtils.translate("&6" + server));
        }
        item.setItemMeta(meta);
        return item;
    }

    private GuiElement.Action getInventoryServerAction(String server) {
        return click -> connectToServer(server);
    }

    private boolean connectToServer(String server) {
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
}
