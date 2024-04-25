package prisongame.prisongame.listeners;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

public class EntityDamageListener implements Listener {
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL) && event.getEntity() instanceof LivingEntity le) {
            if (le.hasPotionEffect(PotionEffectType.JUMP)) {
                event.setCancelled(true);
            }
        }
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            p.setCooldown(Material.IRON_DOOR, 75);
            if (p.getCustomName().contains("ASCENDING")) {
                event.setCancelled(true);
            }
        }
    }
}
