package xyz.mlhmz.lobbyutilities.listener;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import xyz.mlhmz.lobbyutilities.item.NavigatorItem;
import xyz.mlhmz.lobbyutilities.util.ChatUtils;

import java.util.Objects;

/**
 * Events related to the lobby logic
 */
public class LobbyEventListener implements Listener {
    private final LobbyUtilities plugin;

    public LobbyEventListener(LobbyUtilities plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (!isPlayerAtSpawn(p)) return;

        if (!isPlayerInBuildMode(p)) {
            p.sendMessage(LobbyUtilities.prefix + "Du kannst keine Blöcke platzieren!");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (!isPlayerAtSpawn(p)) return;

        if (!isPlayerInBuildMode(p)) {
            p.sendMessage(LobbyUtilities.prefix + "Du kannst keine Blöcke abbauen!");
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onInteraction(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!isPlayerAtSpawn(p)) return;

        if (!isPlayerInBuildMode(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        disableItemDropOnSpawn(e, p);
    }

    private void disableItemDropOnSpawn(PlayerDropItemEvent e, Player p) {
        if (isPlayerAtSpawn(p)) {
            p.sendMessage(LobbyUtilities.prefix + "Du kannst keine Items droppen!");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent e) {
        Player p = e.getPlayer();
        disableBedEnterOnSpawn(e, p);
    }

    private void disableBedEnterOnSpawn(PlayerBedEnterEvent e, Player p) {
        if (isPlayerAtSpawn(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        initializePlayer(e, p);
        teleportPlayerToSpawn(p);
        setItemsOfPlayer(p);
    }

    private void setItemsOfPlayer(Player p) {
        if (isPlayerAtSpawn(p)) {
            p.getInventory().clear();
            p.getInventory().setItem(0, NavigatorItem.get(plugin));
        }
    }

    private void teleportPlayerToSpawn(Player p) {
        Location l = plugin.getConfig().getLocation("spawn");

        if (l == null) {
            return;
        }

        p.teleport(l);
    }

    private void initializePlayer(PlayerJoinEvent e, Player p) {
        e.setJoinMessage(ChatUtils.translate("&a+ &7" + e.getPlayer().getName()));
        p.setHealth(20);
        p.setSaturation(20);
        p.setFoodLevel(20);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(ChatUtils.translate("&c- &7" + e.getPlayer().getName()));
    }

    @EventHandler
    public void onHandSwap(PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();
        disableHandSwapOnSpawn(e, p);
    }

    private void disableHandSwapOnSpawn(PlayerSwapHandItemsEvent e, Player p) {
        if (isPlayerAtSpawn(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        Entity et = e.getEntity();
        disableEntityDamageOnSpawn(e, et);
    }

    private void disableEntityDamageOnSpawn(EntityDamageEvent e, Entity et) {
        if (isEntityAtSpawn(et)) {
            e.setCancelled(LobbyUtilities.cancelledMobDamage);
        }
    }

    @EventHandler
    public void onEntityByEntityDamage(EntityDamageByEntityEvent e) {
        Entity et = e.getEntity();
        disableEntityByEntityDamageOnSpawn(e, et);
    }

    private void disableEntityByEntityDamageOnSpawn(EntityDamageByEntityEvent e, Entity et) {
        if (isEntityAtSpawn(et)) {
            e.setCancelled(LobbyUtilities.cancelledMobDamage);
        }
    }

    @EventHandler
    public void onEntityByBlockDamage(EntityDamageByBlockEvent e) {
        Entity et = e.getEntity();
        disableEntityByBlockDamageOnSpawn(e, et);
    }

    private void disableEntityByBlockDamageOnSpawn(EntityDamageByBlockEvent e, Entity et) {
        if (isEntityAtSpawn(et)) {
            e.setCancelled(LobbyUtilities.cancelledMobDamage);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        Player p = (Player) e.getEntity();
        disableFoodLevelChangeOnSpawn(e, p);
    }

    private void disableFoodLevelChangeOnSpawn(FoodLevelChangeEvent e, Player p) {
        if (isPlayerAtSpawn(p)) {
            p.setFoodLevel(20);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();

        // Removes the
        LobbyUtilities.builderList.remove(p.getUniqueId());

        if (isPlayerAtSpawn(p)) {
            onWorldSwap(p);
            setItemsOfPlayer(p);
        }

        p.setAllowFlight(false);

    }


    private void onWorldSwap(Player p) {
        p.getInventory().clear();
    }

    @EventHandler
    public void onDeathDrop(PlayerDeathEvent e) {
        Player p = e.getEntity().getPlayer();
        disableInventoryDropIfPlayerAtSpawn(e, p);
    }

    private void disableInventoryDropIfPlayerAtSpawn(PlayerDeathEvent e, Player p) {
        if (isPlayerAtSpawn(p)) {
            e.getDrops().clear();
        }
    }

    @EventHandler
    public void onItemDrag(InventoryDragEvent e) {
        Player p = (Player) e.getWhoClicked();
        disableInventoryDragWhenNoBuilderMode(e, p);
    }

    /**
     * checks if the player is in builder mode and if not, cancels the inventory drag
     * @param e the drag event
     * @param p the player to check
     */
    private void disableInventoryDragWhenNoBuilderMode(InventoryDragEvent e, Player p) {
        if (!isPlayerInBuildMode(p)) {
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent e) {
        Player p = e.getPlayer();
        doubleJumpExecutionAtSpawn(e, p);
    }

    /**
     * toggles flight on double-space, cancels the flight event,
     * disables flying and launches the player by setting the velocity
     *
     * @param e the flight event
     * @param p the player to launch
     */
    private void doubleJumpExecutionAtSpawn(PlayerToggleFlightEvent e, Player p) {
        if(isPlayerAtSpawn(p)) {
            if (p.getGameMode() != GameMode.CREATIVE) {
                e.setCancelled(true);
                p.setAllowFlight(false);
                p.setFlying(false);
                p.setVelocity(p.getLocation().getDirection().multiply(2).setY(1));
                p.playSound(p.getEyeLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 10, 1);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        doubleJumpPreparationAtSpawn(p);
    }

    /**
     * allows the player to trigger the flight event in order to launch
     * supresses deprecation warning because there's not really a fix to the
     * deprecation of {@link Player#isOnGround()}
     *
     * @param p the player to launch
     */
    @SuppressWarnings("deprecation")
    private void doubleJumpPreparationAtSpawn(Player p) {
        if(isPlayerAtSpawn(p)) {
            if (p.getGameMode() != GameMode.CREATIVE && p.isOnGround()) {
                p.setAllowFlight(true);
            }
        }
    }

    @EventHandler
    public void onArmorStandManipulationEvent(PlayerArmorStandManipulateEvent e) {
        Player p = e.getPlayer();
        disableArmorStandManipulationOnSpawn(e, p);
    }

    private void disableArmorStandManipulationOnSpawn(PlayerArmorStandManipulateEvent e, Player p) {
        if (isPlayerAtSpawn(p)) {
            if (isPlayerInBuildMode(p)) {
                return;
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        World w = e.getWorld();
        if (!isWorldEqualToSpawn(w)) {
            return;
        }

        cancelWeatherChange(e, w);
    }

    private void cancelWeatherChange(WeatherChangeEvent e, World w) {
        e.setCancelled(true);
        if (w.hasStorm()) {
            w.setWeatherDuration(0);
        }
    }

    /**
     * checks if player is in buildList
     *
     * @param p the player to check on
     * @return boolean if player is in build mode
     */
    private boolean isPlayerInBuildMode(Player p) {
        return LobbyUtilities.builderList.contains(p.getUniqueId());
    }

    /**
     * check if the player-entity is at spawn
     * redundant method for the player in order to avoid confusions with the method
     *
     * @param p the player entity
     * @return boolean if the player is at spawn
     */
    private boolean isPlayerAtSpawn(Player p) {
        return isEntityAtSpawn(p);
    }

    /**
     * check if the entity is at spawn
     * redundant method for the entity in order to avoid confusions with the method
     *
     * @param e the player entity
     * @return boolean if the player is at spawn
     */
    private boolean isEntityAtSpawn(Entity e) {
        return isWorldEqualToSpawn(e.getWorld());
    }

    /**
     * check if the world is the spawn
     *
     * @param w the world to check
     * @return boolean if it is
     */
    private boolean isWorldEqualToSpawn(World w) {
        Location spawn = plugin.getConfig().getLocation("spawn");

        if (Objects.isNull(spawn)) {
            return false;
        }

        return w.equals(spawn.getWorld());
    }
}

