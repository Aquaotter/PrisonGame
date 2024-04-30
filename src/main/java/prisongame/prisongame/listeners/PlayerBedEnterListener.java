package prisongame.prisongame.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class PlayerBedEnterListener implements Listener {
    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent e) {
        Player player = e.getPlayer();

        for (Entity passenger : player.getPassengers()) {
            if (!(passenger instanceof Player))
                continue;

            Player p = (Player) passenger;

            e.setCancelled(true);
            p.leaveVehicle();
            p.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
            p.removePotionEffect(PotionEffectType.WEAKNESS);
            p.removePotionEffect(PotionEffectType.BLINDNESS);
            p.setVelocity(new Vector(0, 0, 0));
            p.teleport(e.getBed().getLocation());

            Plugin instance = Bukkit.getPluginManager().getPlugin("PrisonGame");
            Bukkit.getScheduler().runTaskLater(
                    instance,
                    () -> p.sleep(e.getBed().getLocation(), true),
                    1L);

            break;
        }
    }
}
