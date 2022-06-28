package xyz.mlhmz.lobbyutilities;

import xyz.mlhmz.lobbyutilities.commands.BuildCommand;
import xyz.mlhmz.lobbyutilities.commands.SpawnCommand;
import xyz.mlhmz.lobbyutilities.commands.SpawnUtilsCommand;
import xyz.mlhmz.lobbyutilities.utils.ChatUtils;
import xyz.mlhmz.lobbyutilities.utils.Items;
import xyz.mlhmz.lobbyutilities.listeners.LobbyEvents;
import xyz.mlhmz.lobbyutilities.listeners.Chat;
import xyz.mlhmz.lobbyutilities.listeners.NavigatorListener;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public final class LobbyUtilities extends JavaPlugin {

    public static String prefix = ChatUtils.translate("§2Lobby §8§l» §7");
    public static ArrayList<UUID> builderlist = new ArrayList<UUID>();
    public static boolean cancelledMobDamage = true;
    public static Items items = new Items();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        String configPrefix = getConfig().getString("prefix");

        // Execute Config Code
        if (configPrefix.isEmpty()) {
            prefix = ChatUtils.translate(getConfig().getString("prefix")) + " ";
        }

        new Chat(this);
        new BuildCommand(this);
        new LobbyEvents(this);
        new SpawnUtilsCommand(this);
        new SpawnCommand(this);
        new NavigatorListener(this);

        // Navigator Item
        ItemStack itemStack = new ItemStack(Material.getMaterial(getConfig().getString("navigatoritem.material")));
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(ChatUtils.translate(getConfig().getString("navigatoritem.name")));
        itemStack.setItemMeta(itemMeta);
        items.setNavigatorItem(itemStack);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
