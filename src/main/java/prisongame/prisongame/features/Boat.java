package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import prisongame.prisongame.PrisonGame;

public class Boat implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        if(PrisonGame.active.getName().equals("Boat")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getLocation().getBlockY() <= -53 && PrisonGame.active.getName().equals("Boat") && p.isInWater()) {
                    p.damage(1);
                    p.setNoDamageTicks(1);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only prison:dewater");
                }
            }

        }
    }
}
