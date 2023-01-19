package prisongame.prisongame.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;
import prisongame.prisongame.PrisonGame;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerQuitEvent event) {
        if (event.getPlayer().isInsideVehicle()) {
            if (event.getPlayer().getVehicle() instanceof Player e) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + event.getPlayer().getName() + " 30m Headbugging (AUTOBAN, QUIT REASON:" + event.getReason().name() + ")");
            }
        }
        if (PrisonGame.hardmode.get(event.getPlayer())) {
            event.getPlayer().getPersistentDataContainer().set(PrisonGame.mny, PersistentDataType.DOUBLE, event.getPlayer().getPersistentDataContainer().getOrDefault(PrisonGame.bckupmny, PersistentDataType.DOUBLE, 0.0));
        }
        if (event.getPlayer() == PrisonGame.warden) {
            Bukkit.broadcastMessage(ChatColor.GREEN + "The warden has left the game!");
            PrisonGame.wardenCooldown = 40;
        }
        event.setQuitMessage(ChatColor.GOLD + event.getPlayer().getName() + " ran off somewhere else... (QUIT)");
    }
}
