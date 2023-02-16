package xyz.mlhmz.lobbyutilities.module;

import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import xyz.mlhmz.lobbyutilities.command.ExtendedCommand;

import java.util.List;
import java.util.logging.Level;

public abstract class PluginModule {
    public static final String MODULES_KEY = "modules";
    private final LobbyUtilities plugin;

    public PluginModule(LobbyUtilities plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        if (isModuleEnabled()) {
            registerCommands();
            registerListeners();
            plugin.getLogger().log(Level.INFO, String.format("The module \"%s\" has been enabled.", getIdentifier()));
        } else {
            plugin.getLogger().log(Level.INFO, String.format("The module \"%s\" has not been enabled, because " +
                    "it has been disabled in the Config."));
        }

    }

    private boolean isModuleEnabled() {
        return plugin.getConfig().getBoolean(getModulesConfigKey(), false);
    }

    private String getModulesConfigKey() {
        return String.join(".", MODULES_KEY, getIdentifier());
    }

    private void registerCommands() {
        getCommands().forEach(this::registerCommand);
    }

    private void registerCommand(ExtendedCommand extendedCommand) {
        String commandIdentifier = extendedCommand.getIdentifier();
        PluginCommand command = plugin.getCommand(commandIdentifier);
        if (command != null) {
            command.setExecutor(extendedCommand);
        } else {
            plugin.getLogger().log(Level.SEVERE,
                    String.format("The command with the identifier %s couldn't be loaded.", commandIdentifier));
        }
    }

    private void registerListeners() {
        getListeners().forEach(this::registerListener);
    }

    private void registerListener(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    protected abstract List<Listener> getListeners();
    protected abstract List<ExtendedCommand> getCommands();
    public abstract String getIdentifier();
}
