package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import prisongame.prisongame.PrisonGame;

public class Train implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        if (!PrisonGame.active.getName().equals("Train"))
            return;

        for (var player : Bukkit.getOnlinePlayers())
            if (player.getLocation().getBlockY() == -60 && player.getLocation().clone().subtract(0.0, 1.0, 0.0).getBlock().getType() == Material.SAND && PrisonGame.active.getName().equals("Train") && PrisonGame.isInside(player, PrisonGame.nl("world", 27D, -61D, 920D, 0f, 0f), PrisonGame.nl("world", 129D, 8D, 1041D, 0f, 0f)))
                player.damage(999);
    }
}
