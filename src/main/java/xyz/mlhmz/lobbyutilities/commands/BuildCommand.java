package xyz.mlhmz.lobbyutilities.commands;

import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand implements CommandExecutor {
    private LobbyUtilities plugin;

    public BuildCommand(LobbyUtilities plugin) {
        this.plugin = plugin;

        plugin.getCommand("build").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl ausführen!");
            return true;
        }
        Player p = (Player) sender;
        if(p.hasPermission("lobby.build")) {
            if (p.getWorld() == plugin.getConfig().getLocation("spawn").getWorld()) {
                if(!LobbyUtilities.builderlist.contains(p.getUniqueId())) {
                    p.setGameMode(GameMode.CREATIVE);
                    p.sendMessage(LobbyUtilities.prefix + "Du hast den §2Buildmodus §aangeschaltet§7!");
                    LobbyUtilities.builderlist.add(p.getUniqueId());
                    p.getInventory().clear();
                } else {

                    p.setGameMode(GameMode.SURVIVAL);
                    p.sendMessage(LobbyUtilities.prefix + "Du hast den §2Buildmodus §causgeschaltet§7!");
                    LobbyUtilities.builderlist.remove(p.getUniqueId());
                    p.getInventory().clear();
                    p.getInventory().setItem(0, LobbyUtilities.items.getNavigatorItem());
                    ;
                }
            } else {
                p.sendMessage(LobbyUtilities.prefix + "Du bist nicht in der Lobbywelt.");
            }
            return true;
        } else {
            p.sendMessage(LobbyUtilities.prefix + "§cDu hast keine Rechte dazu!");
        }
        return false;
    }
}
