package xyz.mlhmz.lobbyutilities.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.checkerframework.checker.units.qual.C;
import xyz.mlhmz.lobbyutilities.LobbyUtilities;

/**
 * utils to manage the information scoreboard
 */
public class InfoScoreboardUtils {
    public static final String LOBBY_OBJECTIVE_IDENTIFIER = "lobby";

    public static void showScoreboardAndSchedule(Plugin plugin, Player p) {
        // avoids to run new thread if player already has the scoreboard
        if (p.getScoreboard().getObjective(LOBBY_OBJECTIVE_IDENTIFIER) != null) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                // cancels thread if player is offline
                if (!p.isOnline()) {
                    this.cancel();
                }


                // shows the scoreboard
                show(plugin, p);
            }
        }.runTaskTimer(plugin, 0L, 40L);
    }

    /**
     * Shows the scoreboard to a player
     * @param p the player to show the scoreboard to
     */
    public static void show(Plugin plugin, Player p) {
        // creates a new scoreboard
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        // fallback option, usually shouldn't be null
        assert manager != null;
        Scoreboard board = manager.getNewScoreboard();

        // registration of the scoreboard objective and where it should be shown
        Objective objective = board.registerNewObjective(LOBBY_OBJECTIVE_IDENTIFIER,
                "dummy", ChatUtils.translate("&2mlhmz Server Netzwerk"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        setNormalScores(objective);
        if (p.hasPermission("lobby.admin")) {
            setAdministratorScores(plugin, p, board, objective);
        }

        Location spawn = plugin.getConfig().getLocation("spawn");

        if (spawn == null) return;

        if (p.getWorld() == spawn.getWorld()) {
            p.setScoreboard(board);
        }
    }

    /**
     * Sets scores for normal users
     * @param objective the scoreboard objective
     */
    private static void setNormalScores(Objective objective) {
        setPlaceholder(objective, 7);

        Score playerCount = objective.getScore(ChatUtils.translate("&7Online: &a" + Bukkit.getOnlinePlayers().size() + "/&7" + Bukkit.getMaxPlayers()));
        playerCount.setScore(6);

        setPlaceholder(objective, 5);
    }

    /**
     * Sets scores for admin users
     * @param objective the scoreboard objective
     */
    private static void setAdministratorScores(Plugin plugin, Player p, Scoreboard board, Objective objective) {
        // sets the build score indicator
        Score buildModeScore = objective.getScore(ChatUtils.translate("&7Build-Modus: &caus"));
        if (LobbyUtilities.builderList.contains(p.getUniqueId())) {
            buildModeScore = objective.getScore(ChatUtils.translate("&7Build-Modus: &aan"));
        }
        buildModeScore.setScore(4);

        setPlaceholder(objective, 3);

        // sets the mob damage indicator
        Score mobDamage = objective.getScore(ChatUtils.translate("&7Mobdamage: &aan"));
        if (LobbyUtilities.cancelledMobDamage) {
            mobDamage = objective.getScore(ChatUtils.translate("&7Mobdamage: &caus"));
        }
        mobDamage.setScore(2);

        setPlaceholder(objective, 1);
    }

    /**
     * sets the placeholder
     *
     * @param objective the scoreboard object
     * @param index the index, where the placeholder should go to
     */
    private static void setPlaceholder(Objective objective, int index) {
        Score placeholder = objective.getScore(" ");
        placeholder.setScore(index);
    }
}
