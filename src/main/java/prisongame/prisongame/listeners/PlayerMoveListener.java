package prisongame.prisongame.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;
import prisongame.prisongame.PrisonGame;

public class PlayerMoveListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (PrisonGame.isInside(event.getPlayer(), PrisonGame.active.getFirstTrack().getLocation(), PrisonGame.active.getSecondTrack().getLocation()) && PrisonGame.active.getFirstTrack().getY() > event.getPlayer().getLocation().getY()) {
            PrisonGame.sp.put(event.getPlayer(), PrisonGame.sp.getOrDefault(event.getPlayer(), 0.0) + 0.5);
            event.getPlayer().sendTitle("", ChatColor.GREEN + PrisonGame.sp.get(event.getPlayer()).toString() + "/120", 0, 10, 10);
            if (PrisonGame.sp.get(event.getPlayer()) >= 120) {
                PrisonGame.sp.put(event.getPlayer(), 0.0);
                if (event.getPlayer().hasPotionEffect(PotionEffectType.SPEED)) {
                    event.getPlayer().addPotionEffect(PotionEffectType.SPEED.createEffect(event.getPlayer().getPotionEffect(PotionEffectType.SPEED).getDuration() + 20 * 25, 0));
                } else {
                    event.getPlayer().addPotionEffect(PotionEffectType.SPEED.createEffect(20 * 30, 0));
                }
                if(event.getPlayer().getPotionEffect(PotionEffectType.SPEED).getDuration() >= 20 * 60 * 10){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + event.getPlayer().getName() + " only prison:marathon");
                }
            }
        }
    }
}
