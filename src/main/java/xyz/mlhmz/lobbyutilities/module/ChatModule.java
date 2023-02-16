package xyz.mlhmz.lobbyutilities.module;

import org.bukkit.event.Listener;
import xyz.mlhmz.lobbyutilities.LobbyUtilities;
import xyz.mlhmz.lobbyutilities.command.ExtendedCommand;
import xyz.mlhmz.lobbyutilities.listener.ChatEventListener;
import xyz.mlhmz.lobbyutilities.util.ModuleUtils;

import java.util.List;

public class ChatModule extends PluginModule {
    public ChatModule(LobbyUtilities plugin) {
        super(plugin);
    }

    @Override
    protected List<Listener> getListeners() {
        return ModuleUtils.createListenerList(new ChatEventListener(plugin));
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
        return "chat";
    }
}
