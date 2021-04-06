package me.mlhmz.serverutils.listeners;

import me.mlhmz.serverutils.Serverutils;
import me.mlhmz.serverutils.Utils.items;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
        if (p.getWorld() == Bukkit.getWorld((String) plugin.getConfig().get("lobbyworld"))) {
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
        if (p.getWorld() == Bukkit.getWorld((String) plugin.getConfig().get("lobbyworld"))) {
            p.sendMessage(Serverutils.prefix + "Du kannst keine Items droppen!");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void BedEnterEvent(PlayerBedEnterEvent e) {
        Player p = e.getPlayer();
        if (p.getWorld() == Bukkit.getWorld((String) plugin.getConfig().get("lobbyworld"))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        e.setJoinMessage("§a§l» §7" + e.getPlayer().getName());
        p.setHealth(20);
        p.setSaturation(20);
        Location l = new Location(Bukkit.getWorld((String) plugin.getConfig().get("lobbyworld")), (double) plugin.getConfig().get("x"), (double) plugin.getConfig().get("y"), (double) plugin.getConfig().get("z"), (float) plugin.getConfig().getDouble("yaw"), (float) plugin.getConfig().getDouble("pitch"));
        p.teleport(l);
        if (p.getWorld() == Bukkit.getWorld((String) plugin.getConfig().get("lobbyworld"))) {
            p.playSound(p.getLocation(), MUSIC_DISC_STAL, 1, 1);
        }
        p.getInventory().clear();
        items.givefeather(p, 0);

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage("§c§l« §7" + e.getPlayer().getName());

    }

    @EventHandler
    public void SwapHandEvent(PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();
        if (p.getWorld() == Bukkit.getWorld((String) plugin.getConfig().get("lobbyworld"))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void EntityDamage(EntityDamageEvent e) {
        Entity et = e.getEntity();
        if (et.getWorld() == Bukkit.getWorld((String) plugin.getConfig().get("lobbyworld"))) {
            e.setCancelled(Serverutils.cancelledMobDamage);
        }
    }

    @EventHandler
    public void EntityByEntityDamage(EntityDamageByEntityEvent e) {
        Entity et = e.getEntity();
        if (et.getWorld() == Bukkit.getWorld((String) plugin.getConfig().get("lobbyworld"))) {
            e.setCancelled(Serverutils.cancelledMobDamage);
        }
    }

    @EventHandler
    public void EntityByBlockDamage(EntityDamageByBlockEvent e) {
        Entity et = e.getEntity();
        if (et.getWorld() == Bukkit.getWorld((String) plugin.getConfig().get("lobbyworld"))) {
            e.setCancelled(Serverutils.cancelledMobDamage);
        }
    }

    @EventHandler
    public void HungerEvent(FoodLevelChangeEvent e) {
        Player p = (Player) e.getEntity();
        if (p.getWorld() == Bukkit.getWorld((String) plugin.getConfig().get("lobbyworld"))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        p.stopSound(MUSIC_DISC_STAL);
        p.getInventory().clear();
        if (p.getWorld() == Bukkit.getWorld((String) plugin.getConfig().get("lobbyworld"))) {
            p.playSound(p.getLocation(), MUSIC_DISC_STAL, 1, 1);
            items.givefeather(p, 0);
        }
        p.setAllowFlight(false);

    }

    @EventHandler
    public void DeathDrop(PlayerDeathEvent e) {
        Player p = e.getEntity().getPlayer();
        if (p.getWorld() == Bukkit.getWorld((String) plugin.getConfig().get("lobbyworld"))) {
            e.getDrops().clear();
        }
    }

    @EventHandler
    public void useItemEvent(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getWorld() == Bukkit.getWorld((String) plugin.getConfig().get("lobbyworld"))) {
            if (!Serverutils.builderlist.contains(p.getUniqueId())) {
                Inventory gui = Bukkit.createInventory(null, 9, "§2Navigation");

                // PLACEHOLDER
                ItemStack stained_glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                ItemMeta stained_glass_meta = stained_glass.getItemMeta();
                stained_glass_meta.setDisplayName("§7/");
                stained_glass.setItemMeta(stained_glass_meta);

                // SPAWN ITEM
                ItemStack nether_star = new ItemStack(Material.NETHER_STAR);
                ItemMeta nether_star_meta = nether_star.getItemMeta();
                nether_star_meta.setDisplayName("§2Spawn");
                nether_star.setItemMeta(nether_star_meta);

                // BEDWARS ITEM
                ItemStack red_bed = new ItemStack(Material.RED_BED);
                ItemMeta red_bed_meta = red_bed.getItemMeta();
                red_bed_meta.setDisplayName("§cBedWars");
                red_bed_meta.addEnchant(Enchantment.DIG_SPEED, 3, false);
                red_bed.setItemMeta(red_bed_meta);

                // CHALLENGE ITEM
                ItemStack water_bucket = new ItemStack(Material.WATER_BUCKET);
                ItemMeta water_bucket_meta = water_bucket.getItemMeta();
                water_bucket_meta.setDisplayName("§dChallenge");
                water_bucket.setItemMeta(water_bucket_meta);


                gui.setItem(0, red_bed);
                gui.setItem(1, water_bucket);
                gui.setItem(2, stained_glass);
                gui.setItem(3, stained_glass);
                gui.setItem(4, stained_glass);
                gui.setItem(5, stained_glass);
                gui.setItem(6, stained_glass);
                gui.setItem(7, stained_glass);
                gui.setItem(8, nether_star);

                if (p.getItemInHand().getType() == Material.FEATHER) {
                    p.openInventory(gui);
                }
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void clickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (p.getWorld() == Bukkit.getWorld((String) plugin.getConfig().get("lobbyworld"))) {
            if (!Serverutils.builderlist.contains(p.getUniqueId())) {
                e.setCancelled(true);
            }

            if (e.getView().getTitle().equalsIgnoreCase("§2Navigation")) {
                if (e.getCurrentItem().getType().equals(Material.GRAY_STAINED_GLASS_PANE)) {
                    e.setCancelled(true);
                    return;
                } else if (e.getCurrentItem().getType().equals(Material.NETHER_STAR)) {
                    String world = (String) plugin.getConfig().get("lobbyworld");
                    double x = plugin.getConfig().getDouble("x");
                    double y = plugin.getConfig().getDouble("y");
                    double z = plugin.getConfig().getDouble("z");
                    float yaw = (float) plugin.getConfig().getDouble("yaw");
                    float pitch = (float) plugin.getConfig().getDouble("pitch");
                    Location l = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
                    p.teleport(l);
                    p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                } else if (e.getCurrentItem().getType().equals(Material.RED_BED)) {
                    String world = (String) plugin.getConfig().get("lobbyworld");
                    double x = plugin.getConfig().getDouble("bedwars_x");
                    double y = plugin.getConfig().getDouble("bedwars_y");
                    double z = plugin.getConfig().getDouble("bedwars_z");
                    float yaw = (float) plugin.getConfig().getDouble("bedwars_yaw");
                    float pitch = (float) plugin.getConfig().getDouble("bedwars_pitch");
                    Location l = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
                    p.teleport(l);
                    p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                } else if (e.getCurrentItem().getType().equals(Material.WATER_BUCKET)) {
                    try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        DataOutputStream dos = new DataOutputStream(baos)) {
                        dos.writeUTF("Connect");
                        dos.writeUTF("challenge");
                        p.sendPluginMessage(plugin, "BungeeCord", baos.toByteArray());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
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
                    if (p.getWorld() == Bukkit.getWorld(plugin.getConfig().getString("lobbyworld"))) {
                        p.setScoreboard(board);
                    }

                }
            }
        }.runTaskTimer(plugin, 0L,40L);
    }

    @EventHandler
    public void DoubleJumpEvent (PlayerToggleFlightEvent e) {
        Player p = e.getPlayer();
        if(p.getWorld() == Bukkit.getWorld(plugin.getConfig().getString("lobbyworld"))) {
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
        if(p.getWorld() == Bukkit.getWorld(plugin.getConfig().getString("lobbyworld"))) {
            if (p.getGameMode() != GameMode.CREATIVE && p.isOnGround()) {
                p.setAllowFlight(true);
            }
        }
    }

    @EventHandler
    public void ArmorStand (PlayerArmorStandManipulateEvent e) {
        Player p = e.getPlayer();
        if (p.getWorld() == Bukkit.getWorld(plugin.getConfig().getString("lobbyworld"))) {
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
        if (w.equals(Bukkit.getWorld(plugin.getConfig().getString("spawn.world")))) {
            e.setCancelled(true);
            if (w.hasStorm()) {
                w.setWeatherDuration(0);
            }
        }
    }
}

