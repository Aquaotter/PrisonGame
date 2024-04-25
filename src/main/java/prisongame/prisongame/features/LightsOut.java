package prisongame.prisongame.features;

import org.bukkit.Bukkit;

public class LightsOut implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        for (var player : Bukkit.getOnlinePlayers()) {
            if (player.isSleeping())
                player.setNoDamageTicks(10);
        }
    }
}
