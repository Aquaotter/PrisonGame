package prisongame.prisongame.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class CraftItemListener implements Listener {
    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        var type = event.getRecipe().getResult().getType();

        if (
                type == Material.SHIELD ||
                type == Material.TURTLE_HELMET ||
                type.name().contains("BOAT")
        ) {
            event.setCancelled(true);
        }
    }
}
