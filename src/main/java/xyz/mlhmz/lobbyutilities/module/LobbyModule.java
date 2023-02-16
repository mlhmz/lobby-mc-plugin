package xyz.mlhmz.lobbyutilities.module;

import org.bukkit.event.Listener;
import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import xyz.mlhmz.lobbyutilities.command.ExtendedCommand;
import xyz.mlhmz.lobbyutilities.command.SpawnCommand;
import xyz.mlhmz.lobbyutilities.command.SpawnUtilsCommand;
import xyz.mlhmz.lobbyutilities.listener.LobbyEventListener;
import xyz.mlhmz.lobbyutilities.util.ModuleUtils;

import java.util.List;

public class LobbyModule extends PluginModule {
    public LobbyModule(LobbyUtilities plugin) {
        super(plugin);
    }

    @Override
    protected List<Listener> getListeners() {
        return ModuleUtils.createListenerList(
                new LobbyEventListener(plugin)
        );
    }

    @Override
    protected List<ExtendedCommand> getCommands() {
        return ModuleUtils.createCommandList(
                new SpawnUtilsCommand(plugin),
                new SpawnCommand(plugin)
        );
    }

    @Override
    public boolean isToggleable() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "lobby";
    }
}
