package xyz.mlhmz.lobbyutilities;

import xyz.mlhmz.lobbyutilities.commands.BuildCommand;
import xyz.mlhmz.lobbyutilities.commands.SpawnCommand;
import xyz.mlhmz.lobbyutilities.commands.SpawnUtilsCommand;
import xyz.mlhmz.lobbyutilities.utils.ChatUtils;
import xyz.mlhmz.lobbyutilities.utils.Items;
import xyz.mlhmz.lobbyutilities.listeners.LobbyEventListener;
import xyz.mlhmz.lobbyutilities.listeners.ChatEventListener;
import xyz.mlhmz.lobbyutilities.listeners.NavigatorListener;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public final class LobbyUtilities extends JavaPlugin {

    public static String prefix = ChatUtils.translate("§2Lobby §8§l» §7");
    public static ArrayList<UUID> builderList = new ArrayList<>();
    public static boolean cancelledMobDamage = true;
    public static Items items = new Items();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        // registers messaging channel for bungeecord
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        String configPrefix = getConfig().getString("prefix");

        // Execute Config Code
        if (configPrefix != null && !configPrefix.isEmpty()) {
            prefix = ChatUtils.translate(configPrefix) + " ";
        }

        registerCommandsAndListeners();

        // Navigator Item
        initializeNavigatorItem();
    }

    private void initializeNavigatorItem() {
        String materialIdentifier = getConfig().getString("navigatoritem.material");

        ItemStack navigatorItem;

        if (materialIdentifier == null || materialIdentifier.isEmpty()) {
            getLogger().log(Level.SEVERE, "The navigator is not configured yet... " +
                    "Falling back to the default Option");
            return;
        }



        Material material = Material.getMaterial(materialIdentifier);

        if (material == null) {
            getLogger().log(Level.SEVERE, "The configured navigator item is invalid... " +
                    "Falling back to the default Option");
            material = Material.FEATHER;
        }


        // creates the item stack out of the material by initializing the variable
        navigatorItem = new ItemStack(material);

        // configures the meta of the item
        ItemMeta itemMeta = navigatorItem.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(ChatUtils.translate(getConfig().getString("navigatoritem.name")));
        navigatorItem.setItemMeta(itemMeta);

        // sets the navigator item
        items.setNavigatorItem(navigatorItem);
    }

    private void registerCommandsAndListeners() {
        new ChatEventListener(this);
        new BuildCommand(this);
        new LobbyEventListener(this);
        new SpawnUtilsCommand(this);
        new SpawnCommand(this);
        new NavigatorListener(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        LobbyUtilities.prefix =
                ChatUtils.translate(Objects.requireNonNull(getConfig().getString("prefix"))) + " ";
    }
}
