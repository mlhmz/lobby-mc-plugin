package xyz.mlhmz.lobbyutilities.commands;

import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import xyz.mlhmz.lobbyutilities.utils.ChatUtils;
import xyz.mlhmz.lobbyutilities.utils.Navigator;
import xyz.mlhmz.lobbyutilities.objects.NavigationEntry;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.Objects;

public class SpawnUtilsCommand implements CommandExecutor {
    private final LobbyUtilities plugin;
    private final String COMMAND_NAME = "spawnutils";

    public SpawnUtilsCommand(LobbyUtilities plugin) {
        this.plugin = plugin;

        Objects.requireNonNull(plugin.getCommand(COMMAND_NAME)).setExecutor(this);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Nur Spieler können diesen Befehl ausführen!");
            return true;
        }
        if (!p.hasPermission("serverutils.admin")) {
            p.sendMessage(LobbyUtilities.prefix + ChatUtils.translate("&cDu hast keine Rechte dazu."));
            return true;
        }

        if (args.length == 0) {
            sendCommandUsage(p);
            return true;
        }

        Navigator navigator = new Navigator(plugin);

        switch (args[0]) {
            case "create" -> createNavigatorLocation(args, p, navigator);
            case "delete" -> deleteNavigatorLocation(args, p, navigator);
            case "setspawn" -> setSpawn(p);
            case "reloadconfig" -> reloadConfig(p);
            default -> sendCommandUsage(p);
        }
        return true;
    }

    private void createNavigatorLocation(String[] args, Player p, Navigator navigator) {
        if(args[1] == null) {
            p.sendMessage(
                    LobbyUtilities.prefix +
                    ChatUtils.translate(
                            "&cEs wurde kein Argument gegeben! &7Usage: &6/{cmd} create <Name>"
                                    .replace("{cmd}", COMMAND_NAME)
                    )
            );
        }

        // Creates an Item with the given Name and the Item hold in Hand.
        Material material = p.getInventory().getItemInMainHand().getType();

        if (material.isAir()) {
            p.sendMessage(LobbyUtilities.prefix + ChatUtils.translate("&cDu hast kein Item für den Kompass in der Hand."));
            return;
        }

        // creates a stack out of the material
        ItemStack itemStack = new ItemStack(material);

        // sets the meta of the item
        ItemMeta itemMeta = itemStack.getItemMeta();

        // fallback option, shouldn't usally happen
        if (Objects.isNull(itemMeta)) {
            throw new RuntimeException("The item meta is null");
        }

        itemMeta.setDisplayName(ChatUtils.translate("&6" + args[1]));
        itemStack.setItemMeta(itemMeta);


        // creation of the entry & and check if it already existed with the name
        NavigationEntry navigationEntry = new NavigationEntry(args[1], itemStack, p.getLocation());
        boolean hasCreated = navigator.create(navigationEntry);

        if (!hasCreated) {
            p.sendMessage(
                    LobbyUtilities.prefix +
                    ChatUtils.translate(
                            "&cDie Navigatorlocation &e" + navigationEntry.getName() + " &cgibt es bereits."
                    )
            );
            return;
        }

        p.sendMessage(LobbyUtilities.prefix + ChatUtils.translate("Die Location &6" + navigationEntry.getName() + " &7wurde erstellt!"));
    }

    private void deleteNavigatorLocation(String[] args, Player p, Navigator navigator) {
        if(args[1] == null) {
            p.sendMessage(
                    LobbyUtilities.prefix +
                            ChatUtils.translate(
                                    "&cEs wurde kein Argument gegeben! &7Usage: &6/{cmd} delete <Name>"
                                        .replace("{cmd}", COMMAND_NAME)
                            )
            );
            return;
        }

        boolean hasDeleted = navigator.delete(args[1]);
        if (!hasDeleted) {
            p.sendMessage(LobbyUtilities.prefix + ChatUtils.translate("&cDie Navigatorlocation &e" + args[1] + " &cgibt es bereits."));
        }
    }

    private void setSpawn(Player p) {
        Location l = p.getLocation();

        // fallback option to make sure that the world is not null
        assert l.getWorld() != null;
        l.getWorld().setSpawnLocation(l);

        // sets the spawn and saves the config immediately
        plugin.getConfig().set("spawn", l);
        plugin.saveConfig();

        // Formats the decimals from e.g. 2.174212 to e.g. 2.17
        DecimalFormat df = new DecimalFormat("#.##");

        // Sends a message after all
        p.sendMessage(LobbyUtilities.prefix + "Der Spawn wurde auf die Koordinaten §e" +
                df.format(l.getX()) + " " + df.format(l.getY()) + " " + df.format(l.getZ()) + " §7gesetzt.");
    }

    private void reloadConfig(Player p) {
        plugin.reloadConfig();
        p.sendMessage(LobbyUtilities.prefix + "Die Konfigurationsdatei wurde reloaded.");
    }

    private void sendCommandUsage(Player p) {
        p.sendMessage(LobbyUtilities.prefix + "§7Usage: /{cmd} §e<create|delete|setspawn|reloadconfig>".replace("{cmd}", COMMAND_NAME));
    }
}
