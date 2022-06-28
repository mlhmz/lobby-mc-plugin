package xyz.mlhmz.serverutils.Commands;

import xyz.mlhmz.serverutils.Serverutils;
import xyz.mlhmz.serverutils.Utils.ChatUtils;
import xyz.mlhmz.serverutils.Utils.Navigator;
import xyz.mlhmz.serverutils.objects.NavEntry;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;

public class Spawnutils implements CommandExecutor {
    private Serverutils plugin;
    private final String cmd_name = "spawnutils";

    public Spawnutils(Serverutils plugin) {
        this.plugin = plugin;

        plugin.getCommand("spawnutils").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl ausführen!");
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("serverutils.admin")) {
            p.sendMessage(Serverutils.prefix + ChatUtils.translate("&cDu hast keine Rechte dazu."));
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Serverutils.prefix + "§7Usage: /{cmd} §e<create|delete|setspawn|reloadconfig>".replace("{cmd}", cmd_name));
            return true;
        }

        Navigator navigator = new Navigator(plugin);

        if (args[0].equals("create")) {
            if(args[1] == null) {
                p.sendMessage(Serverutils.prefix + ChatUtils.translate("&cEs wurde kein Argument gegeben! &7Usage: &6/{cmd} create <Name>".replace("{cmd}", cmd_name)));
            }

            // Creating a Item with the given Name and the Item hold in Hand.
            Material material = p.getInventory().getItemInMainHand().getType();

            if (material.isAir()) {
                p.sendMessage(Serverutils.prefix + ChatUtils.translate("&cDu hast kein Item für den Kompass in der Hand."));
                return true;
            }

            ItemStack itemStack = new ItemStack(material);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatUtils.translate("&6" + args[1]));
            itemStack.setItemMeta(itemMeta);


            NavEntry navEntry = new NavEntry(args[1], itemStack, p.getLocation());
            boolean hasCreated = navigator.create(navEntry);
            if (!hasCreated) {
                p.sendMessage(Serverutils.prefix + ChatUtils.translate("&cDie Navigatorlocation &e" + navEntry.getName() + " &cgibt es bereits."));
            }


            p.sendMessage(Serverutils.prefix + ChatUtils.translate("Die Location &6" + navEntry.getName() + " &7wurde erstellt!"));
        } else if (args[0].equals("delete")) {
            if(args[1] == null) {
                p.sendMessage(Serverutils.prefix + ChatUtils.translate("&cEs wurde kein Argument gegeben! &7Usage: &6/{cmd} delete <Name>".replace("{cmd}", cmd_name)));
            }
            boolean hasDeleted = navigator.delete(args[1]);
            if (!hasDeleted) {
                p.sendMessage(Serverutils.prefix + ChatUtils.translate("&cDie Navigatorlocation &e" + args[1] + " &cgibt es bereits."));
            }
        } else if (args[0].equalsIgnoreCase("setspawn")) {
            Location l = p.getLocation();
            plugin.getConfig().set("spawn", l);
            plugin.saveConfig();

            DecimalFormat df = new DecimalFormat("#.##");

            p.sendMessage(Serverutils.prefix + "Der Spawn wurde auf die Koordinaten §e" + df.format(l.getX()) + " " + df.format(l.getY()) + " " + df.format(l.getZ()) + " §7gesetzt.");
            return true;
        } else if (args[0].equalsIgnoreCase("reloadconfig")) {
            plugin.reloadConfig();
            Serverutils.prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix")) + " ";
            p.sendMessage(Serverutils.prefix + "Die Konfigurationsdatei wurde reloaded.");
            return true;
        }
        return true;
    }
}
