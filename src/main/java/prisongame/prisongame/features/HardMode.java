package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.lib.Role;

public class HardMode implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        for (var player : Bukkit.getOnlinePlayers())
            if (PrisonGame.hardmode.get(player)) {
                if (player.isSprinting() && !PrisonGame.escaped.get(player) && PrisonGame.roles.get(player) == Role.PRISONER) {
                    player.setFoodLevel(player.getFoodLevel() - 1);
                    player.sendTitle("", ChatColor.RED + "You can only sprint when you've escaped! [HARD MODE]", 0, 5, 0);
                }
            }
    }
}
