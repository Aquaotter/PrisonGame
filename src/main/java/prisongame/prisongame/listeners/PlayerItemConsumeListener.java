package prisongame.prisongame.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerItemConsumeListener implements Listener {
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (event.getPlayer().hasCooldown(event.getItem().getType()))
            event.setCancelled(true);
        if(event.getPlayer().hasPotionEffect(PotionEffectType.WEAKNESS) || event.getPlayer().hasPotionEffect(PotionEffectType.DOLPHINS_GRACE)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED+"Sorry, You can't eat while being Hand Cuffed!");
            return;
        }
        if (event.getItem().getType().equals(Material.GOLDEN_APPLE)) {
            event.getPlayer().setCooldown(Material.GOLDEN_APPLE, 20 * 60);
        }
    }
}
