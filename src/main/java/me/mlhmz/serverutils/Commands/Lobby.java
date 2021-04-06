package me.mlhmz.serverutils.Commands;

import me.mlhmz.serverutils.Serverutils;
import me.mlhmz.serverutils.Utils.items;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Lobby implements CommandExecutor {
    private Serverutils plugin;

    public Lobby (Serverutils plugin) {
        this.plugin = plugin;

        plugin.getCommand("lobby").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl ausführen!");
            return true;
        }
        Player p = (Player) sender;

        if(args.length == 1 || args.length == 2) {
            if(args[0].equalsIgnoreCase("set")) {
                if(p.hasPermission("lobby.set")) {
                    if (args.length == 1) {
                        World w = p.getWorld();
                        Location l = p.getLocation();
                        plugin.getConfig().set("lobbyworld", w.getName());
                        plugin.getConfig().set("x", l.getX());
                        plugin.getConfig().set("y", l.getY());
                        plugin.getConfig().set("z", l.getZ());
                        plugin.getConfig().set("yaw", l.getYaw());
                        plugin.getConfig().set("pitch", l.getPitch());
                        plugin.saveConfig();

                        p.sendMessage(Serverutils.prefix + "Du hast die Lobby in der Welt §2" + w.getName() + " §7bei X: §2" + (double) l.getX() + " §7Y: §2" + (double) l.getY() + " §7Z: §2" + (double) l.getZ() + " §7gesetzt.");
                    } else if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("bedwars")) {
                            Location l = p.getLocation();
                            plugin.getConfig().set("bedwars_x", l.getX());
                            plugin.getConfig().set("bedwars_y", l.getY());
                            plugin.getConfig().set("bedwars_z", l.getZ());
                            plugin.getConfig().set("bedwars_yaw", l.getYaw());
                            plugin.getConfig().set("bedwars_pitch", l.getPitch());
                            plugin.saveConfig();
                            p.sendMessage(Serverutils.prefix + "Du hast BedWars auf den Koordinaten §2X: " + l.getX() + " §7Y: §2" + l.getY() + "§7Z: §2" + l.getZ());
                        }
                    } else {
                        p.sendMessage(Serverutils.prefix + "§7Den Befehl den du eingegeben hast war ungültig.");
                    }

                } else {
                    p.sendMessage(Serverutils.prefix + "§cDu hast keine Rechte dazu!");
                }
            } else if (args[0].equalsIgnoreCase("damage")){
                if(p.hasPermission("lobby.mobdamage")) {
                    if (Serverutils.cancelledMobDamage) {
                        p.sendMessage(Serverutils.prefix + "Der Mobdamage ist jetzt §caktiviert§7!");
                        Serverutils.cancelledMobDamage = false;
                    } else {
                        p.sendMessage(Serverutils.prefix + "Der Mobdamage ist jetzt §cdeaktiviert§7!");
                        Serverutils.cancelledMobDamage = true;
                    }
                }

            } else if (args[0].equalsIgnoreCase("help")) {
                for (int i = 0; i < 3; i++) {
                    p.sendMessage("");
                }
                p.sendMessage("§8§l» §2Lobby§7 Commands");
                p.sendMessage("§2/lobby §7- Teleportiert dich zur Lobby zurück.");
                p.sendMessage("§7Statt /lobby kann man auch /hub und /l verwenden.");
                p.sendMessage("");

                if (p.hasPermission("lobby.admin")) {
                    p.sendMessage("§8§l» §cAdmin§7 Commands");
                    p.sendMessage("§2/build §7- setzt dich in den Kreativmodus.");
                    p.sendMessage("§2/lobby damage §7- macht Mob und Playerdamage an. §4DEBUG COMMAND");
                    return true;
                }

            } else {
                p.sendMessage(Serverutils.prefix + "§7Den Befehl den du eingegeben hast war ungültig.");
            }
        } else if (args.length == 0) {
            if ((String) plugin.getConfig().get("lobbyworld") == null) {
                p.sendMessage(Serverutils.prefix + "Es wurde noch keine Lobby eingerichtet.");
                return true;
            }
            String world = (String) plugin.getConfig().get("lobbyworld");
            double x = plugin.getConfig().getDouble("x");
            double y = plugin.getConfig().getDouble("y");
            double z = plugin.getConfig().getDouble("z");
            float yaw = (float) plugin.getConfig().getDouble("yaw");
            float pitch = (float) plugin.getConfig().getDouble("pitch");

            Location l = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
            p.teleport(l);
            p.sendMessage(Serverutils.prefix + "Du wurdest zu der Lobby teleportiert!");
            p.getInventory().clear();
            items.givefeather(p, 0);





        } else {
            p.sendMessage(Serverutils.prefix + "§7Den Befehl den du eingegeben hast war ungültig.");
        }
        return false;
    }
}
