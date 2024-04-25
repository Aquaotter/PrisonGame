package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.lib.Role;

public class Nurse implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        for (var player : Bukkit.getOnlinePlayers())
            if (PrisonGame.roles.get(player) == Role.NURSE) {
                if (!player.getInventory().contains(Material.SPLASH_POTION)) {
                    ItemStack pot = new ItemStack(Material.SPLASH_POTION);
                    PotionMeta potionMeta = (PotionMeta) pot.getItemMeta();
                    potionMeta.addCustomEffect(PotionEffectType.HEAL.createEffect(10, 0), true);
                    pot.setItemMeta(potionMeta);

                    player.getInventory().addItem(pot);
                    player.setCooldown(Material.SPLASH_POTION, 40);
                }
            }
    }
}
