package xyz.mlhmz.lobbyutilities.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NavigationEntry {
    private String name;
    private ItemStack item;
    private Location location;
}
