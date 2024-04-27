package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import prisongame.prisongame.PrisonGame;

public class Prefix implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        for (var player : Bukkit.getOnlinePlayers())
            if (!PrisonGame.hardmode.get(player)) {
                if (PrisonGame.api.getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrefix() != null) {
                    if (!player.getDisplayName().contains(PrisonGame.api.getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrefix())) {
                        player.setCustomName(ChatColor.GRAY + "[" + PrisonGame.api.getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrefix() + ChatColor.GRAY + "] " + player.getDisplayName());
                        player.setPlayerListName(ChatColor.GRAY + "[" + PrisonGame.api.getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrefix() + ChatColor.GRAY + "] " + player.getDisplayName());
                        player.setDisplayName(ChatColor.GRAY + "[" + PrisonGame.api.getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrefix() + ChatColor.GRAY + "] " + player.getDisplayName());
                    }
                }
            } else {
                player.setPlayerListName(ChatColor.GRAY + "[" + ChatColor.RED + "HARD MODE" + ChatColor.GRAY + "] " + ChatColor.GRAY + player.getName());
            }
    }
}
