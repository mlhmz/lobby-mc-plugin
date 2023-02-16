package xyz.mlhmz.lobbyutilities.module;

import org.bukkit.event.Listener;
import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import xyz.mlhmz.lobbyutilities.command.ExtendedCommand;
import xyz.mlhmz.lobbyutilities.listener.ScoreboardListener;
import xyz.mlhmz.lobbyutilities.util.ModuleUtils;

import java.util.List;

public class ScoreboardModule extends PluginModule {
    public ScoreboardModule(LobbyUtilities plugin) {
        super(plugin);
    }

    @Override
    protected List<Listener> getListeners() {
        return ModuleUtils.createListenerList(
                new ScoreboardListener(plugin)
        );
    }

    @Override
    protected List<ExtendedCommand> getCommands() {
        return null;
    }

    @Override
    public boolean isToggleable() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "scoreboard";
    }
}
