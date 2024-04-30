package prisongame.prisongame.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import prisongame.prisongame.PrisonGame;

import static prisongame.prisongame.config.ConfigKt.getConfig;

public class PlayerChatListener implements Listener {
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        if (PrisonGame.isInside(event.getPlayer(), new Location(Bukkit.getWorld("world"), 2141, -57, -2084), new Location(Bukkit.getWorld("world"), 2137, -62, -2080))) {
            if (event.getMessage().equals("1775182") && PrisonGame.warden.equals(event.getPlayer())) {
                if (PrisonGame.swapcool <= 0) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Access granted.");
                    InventoryClickListener.switchMap(getConfig().getPrisons().get("boat"));
                } else {
                    event.getPlayer().sendMessage(ChatColor.RED + "That's on cooldown! " + ChatColor.YELLOW + PrisonGame.swapcool / 20 + " seconds left.");
                }
            }
        }
    }
}
