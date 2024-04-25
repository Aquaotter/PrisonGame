package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import prisongame.prisongame.PrisonGame;

public class Boat implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        if (!PrisonGame.active.getName().equals("Boat"))
            return;

        for (var player : Bukkit.getOnlinePlayers())
            if (player.getY() <= -53 && player.isInWater()) {
                player.setNoDamageTicks(1);
                player.damage(1);
            }
    }
}
