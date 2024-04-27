package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffectType;
import prisongame.prisongame.keys.Keys;

public class NightVision implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        for (var player : Bukkit.getOnlinePlayers())
            if (Keys.NIGHT_VISION.has(player))
                player.addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(99999, 255));
            else
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }
}
