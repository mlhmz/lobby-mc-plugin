package me.mlhmz.serverutils;

import me.mlhmz.serverutils.Commands.Creative;
import me.mlhmz.serverutils.Commands.Spawn;
import me.mlhmz.serverutils.Commands.Spawnutils;
import me.mlhmz.serverutils.Utils.ChatUtils;
import me.mlhmz.serverutils.Utils.Items;
import me.mlhmz.serverutils.listeners.LobbyEvents;
import me.mlhmz.serverutils.listeners.Chat;
import me.mlhmz.serverutils.listeners.NavigatorListener;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public final class Serverutils extends JavaPlugin {

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
        new Creative(this);
        new LobbyEvents(this);
        new Spawnutils(this);
        new Spawn(this);
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
