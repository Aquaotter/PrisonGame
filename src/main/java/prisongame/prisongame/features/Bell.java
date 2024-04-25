package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

public class Bell implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        for (var player : Bukkit.getOnlinePlayers())
            if (player.getInventory().getHelmet() != null) {
                if (!player.getInventory().getHelmet().getType().equals(Material.YELLOW_WOOL)) {
                    player.setMaxHealth(20);
                } else {
                    player.addPotionEffect(PotionEffectType.SPEED.createEffect(20, 2));
                }
            } else {
                player.setMaxHealth(20);
            }
    }
}
