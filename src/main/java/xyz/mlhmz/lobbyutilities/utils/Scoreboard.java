package xyz.mlhmz.lobbyutilities.utils;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Scoreboard {
    public static void show(Plugin plugin) {
        new BukkitRunnable() {


            @Override
            public void run() {




            }
        }.runTaskTimer(plugin, 0L, 100L);
    }
}
