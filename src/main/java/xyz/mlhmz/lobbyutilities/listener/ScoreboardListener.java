package xyz.mlhmz.lobbyutilities.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import xyz.mlhmz.lobbyutilities.util.InfoScoreboardUtils;

public class ScoreboardListener implements Listener {
    private final LobbyUtilities plugin;

    public ScoreboardListener(LobbyUtilities plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        createScoreboard(e.getPlayer());
    }


    private void createScoreboard(Player p) {
        InfoScoreboardUtils.showScoreboardAndSchedule(plugin, p);
    }
}
