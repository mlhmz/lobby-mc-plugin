package me.mlhmz.serverutils.listeners;

import me.mlhmz.serverutils.Serverutils;
import me.mlhmz.serverutils.Utils.Items;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import static org.bukkit.Sound.*;

public class LobbyEvents implements Listener {
    private Serverutils plugin;

    public LobbyEvents(Serverutils plugin) {
        this.plugin = plugin;

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (p.getWorld() == plugin.getConfig().getLocation("spawn").getWorld()) {
            if (!Serverutils.builderlist.contains(p.getUniqueId())) {
                p.sendMessage(Serverutils.prefix + "Du kannst keine Blöcke abbauen!");
                e.setCancelled(true);
                return;
            }
        }

    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (p.getWorld() == plugin.getConfig().getLocation("spawn").getWorld()) {
            p.sendMessage(Serverutils.prefix + "Du kannst keine Items droppen!");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void BedEnterEvent(PlayerBedEnterEvent e) {
        Player p = e.getPlayer();
        if (p.getWorld() == plugin.getConfig().getLocation("spawn").getWorld()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        e.setJoinMessage("§a§l» §7" + e.getPlayer().getName());
        p.setHealth(20);
        p.setSaturation(20);
        Location l = plugin.getConfig().getLocation("spawn");
        p.teleport(l);
        if (p.getWorld() == plugin.getConfig().getLocation("spawn").getWorld()) {
            p.playSound(p.getLocation(), MUSIC_DISC_STAL, 1, 1);
        }
        p.getInventory().clear();
        Items items = new Items();

        p.getInventory().setItem(0, Serverutils.items.getNavigatorItem());

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage("§c§l« §7" + e.getPlayer().getName());

    }

    @EventHandler
    public void SwapHandEvent(PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();
        if (p.getWorld() == plugin.getConfig().getLocation("spawn").getWorld()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void EntityDamage(EntityDamageEvent e) {
        Entity et = e.getEntity();
        if (et.getWorld() == plugin.getConfig().getLocation("spawn").getWorld()) {
            e.setCancelled(Serverutils.cancelledMobDamage);
        }
    }

    @EventHandler
    public void EntityByEntityDamage(EntityDamageByEntityEvent e) {
        Entity et = e.getEntity();
        if (et.getWorld() == plugin.getConfig().getLocation("spawn").getWorld()) {
            e.setCancelled(Serverutils.cancelledMobDamage);
        }
    }

    @EventHandler
    public void EntityByBlockDamage(EntityDamageByBlockEvent e) {
        Entity et = e.getEntity();
        if (et.getWorld() == plugin.getConfig().getLocation("spawn").getWorld()) {
            e.setCancelled(Serverutils.cancelledMobDamage);
        }
    }

    @EventHandler
    public void HungerEvent(FoodLevelChangeEvent e) {
        Player p = (Player) e.getEntity();
        if (p.getWorld() == plugin.getConfig().getLocation("spawn").getWorld()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();

        if (Serverutils.builderlist.contains(p.getUniqueId())) {
            Serverutils.builderlist.remove(p.getUniqueId());
        }

        p.stopSound(MUSIC_DISC_STAL);
        p.getInventory().clear();
        if (p.getWorld() == plugin.getConfig().getLocation("spawn").getWorld()) {

            p.playSound(p.getLocation(), MUSIC_DISC_STAL, 1, 1);
            p.getInventory().setItem(0, Serverutils.items.getNavigatorItem());
        }
        p.setAllowFlight(false);

    }

    @EventHandler
    public void DeathDrop(PlayerDeathEvent e) {
        Player p = e.getEntity().getPlayer();
        if (p.getWorld() == plugin.getConfig().getLocation("spawn").getWorld()) {
            e.getDrops().clear();
        }
    }

    @EventHandler
    public void ItemEvent(InventoryDragEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (Serverutils.builderlist.contains(p.getUniqueId())) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void ScoreboardEvent(PlayerJoinEvent e) {


        new BukkitRunnable() {

            @Override
            public void run() {
                Player p = e.getPlayer();
                ScoreboardManager mang = Bukkit.getScoreboardManager();
                Scoreboard board = mang.getNewScoreboard();

                Objective obj = board.registerNewObjective("lobby", "dummy", "§2mlhmz Server Netzwerk");
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);

                Score placeholder1 = obj.getScore("");
                placeholder1.setScore(7);

                Score playercount = obj.getScore("§7Online: §a" + Bukkit.getOnlinePlayers().size() + "/§7" + Bukkit.getMaxPlayers());
                playercount.setScore(6);

                Score placeholder2 = obj.getScore("");
                placeholder2.setScore(5);

                if (p.hasPermission("lobby.admin")) {
                    Score buildmode;
                    if (Serverutils.builderlist.contains(p.getUniqueId())) {
                        buildmode = obj.getScore("§7Build-Modus: §aan");
                    } else {
                        buildmode = obj.getScore("§7Build-Modus: §caus");
                    }
                    buildmode.setScore(4);

                    Score placeholder3 = obj.getScore("");
                    placeholder3.setScore(3);

                    Score mobdmg;
                    if (Serverutils.cancelledMobDamage) {
                        mobdmg = obj.getScore("§7Mobdamage: §caus");
                    } else {
                        mobdmg = obj.getScore("§7Mobdamage: §aan");

                    }
                    mobdmg.setScore(2);

                    Score placeholder4 = obj.getScore("");
                    placeholder4.setScore(1);
                    if (p.getWorld() == plugin.getConfig().getLocation("spawn").getWorld()) {
                        p.setScoreboard(board);
                    }

                }
            }
        }.runTaskTimer(plugin, 0L,40L);
    }

    @EventHandler
    public void DoubleJumpEvent (PlayerToggleFlightEvent e) {
        Player p = e.getPlayer();
        if(p.getWorld() == plugin.getConfig().getLocation("spawn").getWorld()) {
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
    public void DoubleJumpMoveEvent (PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if(p.getWorld() == plugin.getConfig().getLocation("spawn").getWorld()) {
            if (p.getGameMode() != GameMode.CREATIVE && p.isOnGround()) {
                p.setAllowFlight(true);
            }
        }
    }

    @EventHandler
    public void ArmorStand (PlayerArmorStandManipulateEvent e) {
        Player p = e.getPlayer();
        if (p.getWorld() == plugin.getConfig().getLocation("spawn").getWorld()) {
            if (Serverutils.builderlist.contains(p.getUniqueId())) {
                return;
            }
            p.sendMessage("§8Nö");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void weatherChange(WeatherChangeEvent e) {
        World w = e.getWorld();
        if (w.equals(plugin.getConfig().getLocation("spawn").getWorld())) {
            e.setCancelled(true);
            if (w.hasStorm()) {
                w.setWeatherDuration(0);
            }
        }
    }
}

