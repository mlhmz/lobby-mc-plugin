package xyz.mlhmz.lobbyutilities;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.mlhmz.lobbyutilities.module.*;
import xyz.mlhmz.lobbyutilities.util.ChatUtils;
import xyz.mlhmz.lobbyutilities.util.ModuleUtils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public final class LobbyUtilities extends JavaPlugin {

    public static String prefix = ChatUtils.translate("&2Lobby&8: &7");
    public static ArrayList<UUID> builderList = new ArrayList<>();
    public static boolean cancelledMobDamage = true;

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

        updateModules();
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        updateModules();
        LobbyUtilities.prefix =
                ChatUtils.translate(Objects.requireNonNull(getConfig().getString("prefix"))) + " ";
    }

    private void updateModules() {
        ModuleUtils.createModuleList(
                new LobbyModule(this),
                new ScoreboardModule(this),
                new ChatModule(this),
                new NavigatorModule(this),
                new LobbyModule(this)
        ).forEach(PluginModule::initialize);
    }
}
