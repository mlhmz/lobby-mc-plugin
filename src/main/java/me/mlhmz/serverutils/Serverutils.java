package me.mlhmz.serverutils;

import me.mlhmz.serverutils.Commands.Creative;
import me.mlhmz.serverutils.Commands.Lobby;
import me.mlhmz.serverutils.Utils.ChatUtils;
import me.mlhmz.serverutils.listeners.LobbyEvents;
import me.mlhmz.serverutils.listeners.Chat;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public final class Serverutils extends JavaPlugin {

    public static String prefix = ChatUtils.translate("§2Lobby §8§l» §7");
    public static ArrayList<UUID> builderlist = new ArrayList<UUID>();
    public static boolean cancelledMobDamage = true;

    @Override
    public void onEnable() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        String configPrefix = getConfig().getString("prefix");

        // Execute Config Code
        if (configPrefix.isEmpty()) {
            prefix = ChatUtils.translate(getConfig().getString("prefix"));
        }


        new Chat(this);
        new Creative(this);
        new LobbyEvents(this);
        new Lobby(this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
