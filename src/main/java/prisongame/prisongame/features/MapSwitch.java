package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.listeners.InventoryClickListener;

import static prisongame.prisongame.config.ConfigKt.getConfig;

public class MapSwitch implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (PrisonGame.isInside(player, new Location(Bukkit.getWorld("world"), 1453, -41, 1533), new Location(Bukkit.getWorld("world"), 1455, -41, 1535))) {
                if (PrisonGame.warden.equals(player)) {
                    if (PrisonGame.swapcool <= 0) {
                        InventoryClickListener.switchMap(getConfig().getPrisons().get("polus"));
                    } else {
                        player.sendTitle(ChatColor.YELLOW + ("" + PrisonGame.swapcool / 20) + " seconds left.", ChatColor.RED + "That's on cooldown! ", 0, 50, 0);
                    }
                } else {
                    player.sendTitle("", ChatColor.RED + "YOU MUST BE WARDEN!", 0, 50, 0);
                }
            }

            if (PrisonGame.isInside(player, new Location(Bukkit.getWorld("world"), -2011, -53, -1929), new Location(Bukkit.getWorld("world"), -2009, -57, -1931))) {
                if (PrisonGame.warden.equals(player)) {
                    if (PrisonGame.swapcool <= 0)
                        InventoryClickListener.switchMap(getConfig().getPrisons().get("nether"));
                    else {
                        player.sendTitle(ChatColor.YELLOW + ("" + PrisonGame.swapcool / 20) + " seconds left.", ChatColor.RED + "That's on cooldown! ", 0, 50, 0);
                    }
                } else {
                    player.sendTitle("", ChatColor.RED + "YOU MUST BE WARDEN!", 0, 50, 0);
                }
            }
        }
    }
}
