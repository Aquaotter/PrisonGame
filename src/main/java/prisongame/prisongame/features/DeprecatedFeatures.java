package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import prisongame.prisongame.keys.Keys;

/**
 * Handles old data from deprecated features.
 */
public class DeprecatedFeatures implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        for (var player : Bukkit.getOnlinePlayers()) {
            if (Keys.HEAD_GUARD.has(player))
                Keys.HEAD_GUARD.remove(player);

            if (Keys.MUTED.has(player))
                Keys.MUTED.remove(player);

            if (Keys.SEMICLOAK.has(player)) {
                Keys.MONEY.set(player, Keys.MONEY.get(player, 0.0) + 1000);
                player.sendMessage(ChatColor.RED + "Your semicloak ascension has been refunded and removed as ascensions have been removed!");
                Keys.SEMICLOAK.remove(player);
            }
            if (Keys.REINFORCEMENT.has(player)) {
                Keys.MONEY.set(player, Keys.MONEY.get(player, 0.0) + 1000);
                player.sendMessage(ChatColor.RED + "Your Reinforcement ascension has been refunded and removed as ascensions have been removed!");
                Keys.REINFORCEMENT.remove(player);
            }
            if (Keys.SPAWN_PROTECTION.has(player)) {
                Keys.MONEY.set(player, Keys.MONEY.get(player, 0.0) + 1000);
                player.sendMessage(ChatColor.RED + "Your protspawn ascension has been refunded and removed as ascensions have been removed!");
                Keys.SPAWN_PROTECTION.remove(player);
            }
            if (Keys.RANDOM_ITEMS.has(player)) {
                player.sendMessage(ChatColor.RED + "Your Random item ascension has been refunded and removed as ascensions have been removed!");
                Keys.MONEY.set(player,Keys.MONEY.get(player, 0.0) + 1000);
                Keys.RANDOM_ITEMS.remove(player);
            }
        }
    }
}
