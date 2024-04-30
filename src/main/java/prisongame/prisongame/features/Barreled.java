package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.lib.Role;

public class Barreled implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        if (!(PrisonGame.active.getName().equals("Barreled")))
            return;
        Schedule.bossBar.setTitle(Schedule.bossBar.getTitle() + ChatColor.GREEN + " Power: " + PrisonGame.BBpower);

        if (Bukkit.getWorld("world").getBlockAt(new Location(Bukkit.getWorld("world"), -1023, -57, -994)).getType().equals(Material.AIR))
            for (Player p : Bukkit.getOnlinePlayers())
                    if (PrisonGame.roles.get(p).equals(Role.PRISONER))
                        p.addPotionEffect(PotionEffectType.SPEED.createEffect(20 * 5, 0));
    }
}
