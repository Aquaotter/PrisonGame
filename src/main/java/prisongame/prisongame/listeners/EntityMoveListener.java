package prisongame.prisongame.listeners;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityMoveListener implements Listener {
    @EventHandler
    public void onEntityMove(EntityMoveEvent event) {
        if (event.getTo().getBlock().getType().equals(Material.MANGROVE_DOOR) && event.getEntity().getType().equals(EntityType.ZOMBIE)) {
            event.setCancelled(true);
            event.getEntity().teleport(new Location(Bukkit.getWorld("world"), -40, -58, -973));
        }
    }
}
