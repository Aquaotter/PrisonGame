package prisongame.prisongame.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.keys.Key;
import prisongame.prisongame.keys.Keys;

public class PlayerInteractAtEntityListener implements Listener {
    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked().getType().equals(EntityType.WANDERING_TRADER)) {
            event.setCancelled(true);
        }
        if (event.getRightClicked().getType().equals(EntityType.VILLAGER)) {
            event.setCancelled(true);
        }
        if (event.getRightClicked().getName().equals("bertrude (real settings)")) {
            PrisonGame.instance.openBertude(true, event.getPlayer());
        }
    }
}
