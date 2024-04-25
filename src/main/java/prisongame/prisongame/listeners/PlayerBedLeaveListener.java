package prisongame.prisongame.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import prisongame.prisongame.features.Schedule;

public class PlayerBedLeaveListener implements Listener {
    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        if (Schedule.bossBar.getTitle().equals("LIGHTS OUT")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You can't wake up until roll call!");
        }
    }
}
