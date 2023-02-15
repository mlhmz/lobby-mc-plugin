package xyz.mlhmz.lobbyutilities.listener;

import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.mlhmz.lobbyutilities.util.ChatUtils;

public class ChatEventListener implements Listener {
    private final LobbyUtilities plugin;

    public ChatEventListener(LobbyUtilities plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncPlayerChatEvent e) {
        e.setFormat(ChatUtils.translate("&e%1$s&8: &7%2$s"));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onMention(AsyncPlayerChatEvent e) {
        Player sender = e.getPlayer();
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (e.getMessage().contains(p.getName())) {
                String message = e.getMessage();
                e.setMessage(ChatUtils.translate(message.replace(p.getName(), "&e@" + p.getName() + "&7" )));
                p.playSound(p.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 5, 1);
                // One second has 20 ticks so everything is multiplied by 20
                p.sendTitle(ChatUtils.translate(String.format("&e%s &7hat dich erwähnt!", sender.getName())),
                        ChatUtils.translate("&8Guck doch mal in den Chat."), 20, 20 * 3, 20);
            }
        }
    }
}
