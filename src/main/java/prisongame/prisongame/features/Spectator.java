package prisongame.prisongame.features;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.potion.PotionEffectType;
import prisongame.prisongame.PrisonGame;

public class Spectator implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        for (var player : Bukkit.getOnlinePlayers()) {
            var gameMode = player.getGameMode();

            if (gameMode.equals(GameMode.SURVIVAL))
                player.setGameMode(GameMode.ADVENTURE);

            if (player.hasPotionEffect(PotionEffectType.LUCK) || player.hasPotionEffect(PotionEffectType.BAD_OMEN)) {
                if (gameMode.equals(GameMode.SPECTATOR)) {
                    if (PrisonGame.warden != null) {
                        player.setSpectatorTarget(PrisonGame.warden);
                        player.sendActionBar(new TextComponent(ChatColor.GRAY + "CURRENTLY SPECTATING THE WARDEN"));
                    } else {
                        PrisonGame.tptoBed(player);
                        player.sendActionBar(new TextComponent(ChatColor.GRAY + "NO WARDEN TO SPECTATE!"));
                    }
                }
            } else {
                if (player.getGameMode().equals(GameMode.SPECTATOR)) {
                    player.setGameMode(GameMode.ADVENTURE);
                }
            }
        }
    }
}
