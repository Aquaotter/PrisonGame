package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffectType;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.lib.Role;

public class BlackMarket implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        for (var player : Bukkit.getOnlinePlayers()) {
            var role = PrisonGame.roles.get(player);

            if (role == Role.PRISONER || role == Role.WARDEN)
                continue;
            if(player.hasPotionEffect(PotionEffectType.UNLUCK)) {
                player.sendMessage(ChatColor.RED + "You can't be in here!");
                player.playSound(player, Sound.ENTITY_PILLAGER_AMBIENT, 1.5f, 0.75f);
                player.damage(6);
                player.removePotionEffect(PotionEffectType.UNLUCK);
                player.teleport(PrisonGame.active.getBlackMarketOut().getLocation());
            }
        }
    }
}
