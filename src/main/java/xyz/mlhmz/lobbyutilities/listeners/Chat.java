package xyz.mlhmz.lobbyutilities.listeners;

import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Chat implements Listener {
    private LobbyUtilities plugin;

    public Chat (LobbyUtilities plugin) {
        this.plugin = plugin;

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public static void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String Message = e.getMessage();
        e.setFormat("§e" + p.getName() + " §8§l» §7" + e.getMessage());
    }

    @EventHandler(priority = EventPriority.LOW)
    public static void onMention(AsyncPlayerChatEvent e) {
        Player sender = e.getPlayer();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (e.getMessage().contains(p.getName())) {
                String Message = e.getMessage();
                e.setMessage(Message.replace(p.getName(), "§e@" + p.getName() + "§7" ));
                p.playSound(p.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 5, 1);
                // One second has 20 ticks so everything is multiplied by 20
                p.sendTitle("§ePaym §7hat dich erwähnt!", "§8Guck doch mal in den Chat.", 20 * 1, 20 * 3, 20 * 1);
            }
        }
    }
}
