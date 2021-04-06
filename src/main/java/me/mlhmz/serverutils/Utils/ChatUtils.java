package me.mlhmz.serverutils.Utils;

import net.md_5.bungee.api.ChatColor;

public class ChatUtils {
    public static String translate(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
