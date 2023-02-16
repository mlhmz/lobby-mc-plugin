package xyz.mlhmz.lobbyutilities;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.mlhmz.lobbyutilities.command.BuildCommand;
import xyz.mlhmz.lobbyutilities.listener.NavigatorListener;
import xyz.mlhmz.lobbyutilities.module.ChatModule;
import xyz.mlhmz.lobbyutilities.module.LobbyModule;
import xyz.mlhmz.lobbyutilities.module.ScoreboardModule;
import xyz.mlhmz.lobbyutilities.util.ChatUtils;

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
        registerCommandsAndListeners();
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
        new LobbyModule(this).initialize();
        new ScoreboardModule(this).initialize();
        new ChatModule(this).initialize();
    }

    private void registerCommandsAndListeners() {
        new BuildCommand(this);
        new NavigatorListener(this);
    }
}
