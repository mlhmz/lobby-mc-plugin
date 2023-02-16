package xyz.mlhmz.lobbyutilities.util;

import org.bukkit.event.Listener;
import xyz.mlhmz.lobbyutilities.command.ExtendedCommand;

import java.util.List;

public class ModuleUtils {
    public static List<ExtendedCommand> createCommandList(ExtendedCommand... commands) {
        return createObjectList(commands);
    }

    public static List<Listener> createListenerList(Listener... listeners) {
        return createObjectList(listeners);
    }

    private static <T> List<T> createObjectList(T[] args) {
        return List.of(args);
    }
}
