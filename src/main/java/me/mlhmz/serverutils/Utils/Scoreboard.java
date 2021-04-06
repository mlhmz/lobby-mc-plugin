package me.mlhmz.serverutils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;

public class Scoreboard {
    public static void show(Plugin plugin) {
        new BukkitRunnable() {


            @Override
            public void run() {




            }
        }.runTaskTimer(plugin, 0L, 100L);
    }
}
