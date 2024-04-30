package prisongame.prisongame.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class PrepareItemCraftListener implements Listener {
    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        var inventory = event.getInventory();
        var recipe = event.getRecipe();

        if (recipe == null)
            return;

        var type = recipe.getResult().getType();

        if (
                type == Material.SHIELD ||
                type == Material.TURTLE_HELMET ||
                type.name().contains("BOAT")
        ) {
            inventory.setItem(0, new ItemStack(Material.AIR));
        }
    }
}
