package prisongame.prisongame.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.commands.staff.VanishCommand;
import prisongame.prisongame.discord.listeners.Messages;
import prisongame.prisongame.keys.Keys;

import static prisongame.prisongame.config.ConfigKt.getConfig;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!getConfig().getDev() && !event.getPlayer().getPersistentDataContainer().has(VanishCommand.VANISHED))
            Messages.INSTANCE.onLeave(event.getPlayer());

        if (PrisonGame.hardmode.get(event.getPlayer())) {
            Keys.MONEY.set(event.getPlayer(), Keys.BACKUP_MONEY.get(event.getPlayer(), 0.0));
        }
        if (event.getPlayer() == PrisonGame.warden) {
            Bukkit.broadcastMessage(ChatColor.GREEN + "The warden has left the game!");
            PrisonGame.wardenCooldown = 40;
        }
        event.setQuitMessage(ChatColor.GOLD + event.getPlayer().getName() + " ran off somewhere else... (QUIT)");
        if (event.getPlayer().getPersistentDataContainer().has(VanishCommand.VANISHED)) {
            event.setQuitMessage("");
            for(Player p : Bukkit.getOnlinePlayers()){
                if(p.hasPermission("pbb.staff.vanish.seequit")) p.sendMessage(ChatColor.RED+"[STAFF] "+event.getPlayer().getName()+" has left in Vanish!");
            }
        }
    }
}
