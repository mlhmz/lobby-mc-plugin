package xyz.mlhmz.lobbyutilities;

import xyz.mlhmz.lobbyutilities.command.BuildCommand;
import xyz.mlhmz.lobbyutilities.command.SpawnCommand;
import xyz.mlhmz.lobbyutilities.command.SpawnUtilsCommand;
import xyz.mlhmz.lobbyutilities.util.ChatUtils;
import xyz.mlhmz.lobbyutilities.listener.LobbyEventListener;
import xyz.mlhmz.lobbyutilities.listener.ChatEventListener;
import xyz.mlhmz.lobbyutilities.listener.NavigatorListener;
import org.bukkit.plugin.java.JavaPlugin;

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

        registerCommandsAndListeners();
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
