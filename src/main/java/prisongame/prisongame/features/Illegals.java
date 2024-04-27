package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.lib.Role;

public class Illegals implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        for (var player : Bukkit.getOnlinePlayers()) {
            if (player.getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.DAMAGE_ALL)) {
                if (player.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND_SWORD)) {
                    if (PrisonGame.roles.get(player) != Role.WARDEN) {
                        player.sendMessage("no illegal 4 u!");
                        player.getInventory().getItemInMainHand().removeEnchantment(Enchantment.DAMAGE_ALL);
                        player.kickPlayer("");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + player.getName() + " 5m Abuse of Illegals [AUTO] (Diamond Sharp Sword)");
                    }
                }
            }

            if (player.getGameMode() != GameMode.ADVENTURE)
                continue;

            var inventory = player.getInventory();
            inventory.remove(Material.NETHERITE_SWORD);
            inventory.remove(Material.IRON_BARS);
            inventory.remove(Material.SUGAR);

            if (PrisonGame.hardmode.get(player)) {
                inventory.remove(Material.NETHERITE_HELMET);
                inventory.remove(Material.GOLDEN_APPLE);
            }
        }
    }
}
