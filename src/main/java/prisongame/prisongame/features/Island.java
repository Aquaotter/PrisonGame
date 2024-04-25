package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarFlag;
import prisongame.prisongame.PrisonGame;

public class Island implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        if (!PrisonGame.active.getName().equals("Island"))
            return;

        Schedule.bossBar.addFlag(BarFlag.CREATE_FOG);

        for (var player : Bukkit.getOnlinePlayers())
            if (player.getLocation().getBlockY() == -61 && player.isInWater())
                player.damage(4);
    }
}
