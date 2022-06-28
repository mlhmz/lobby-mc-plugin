package xyz.mlhmz.lobbyutilities.utils;

import net.md_5.bungee.api.ChatColor;

public class ChatUtils {
    /**
     * translates colorcodes with & from messages
     *
     * @param s message to translate
     * @return translated colored messages
     */
    public static String translate(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
