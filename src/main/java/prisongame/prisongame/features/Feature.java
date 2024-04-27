package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import prisongame.prisongame.PrisonGame;

/**
 * Handles scheduling tasks & registering listeners for a feature.
 */
public interface Feature {
    BukkitScheduler scheduler = Bukkit.getScheduler();
    PrisonGame plugin = PrisonGame.instance;

    void schedule();
    void execute();
}
