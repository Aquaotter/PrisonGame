package prisongame.prisongame.features;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import oshi.jna.platform.mac.SystemB;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.keys.Keys;

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
                        Integer isDevMode = Keys.DEVMODE.get(player, 0);
                        String rankColor = PrisonGame.api.getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getMetaValue("color");
                        if(rankColor==null)rankColor=ChatColor.RED+"NULL";
                        if(isDevMode == 1){
                            player.setCustomName(ChatColor.GRAY + "[" + PrisonGame.api.getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrefix() + ChatColor.GRAY + "] "+rankColor + player.getName());
                            player.setPlayerListName(ChatColor.GRAY + "[" + PrisonGame.api.getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrefix() + ChatColor.GRAY + "] "+rankColor + player.getName());
                            player.setDisplayName(ChatColor.GRAY + "[" + PrisonGame.api.getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrefix() + ChatColor.GRAY + "] "+rankColor + player.getName());
                        }else{
                        player.setCustomName(ChatColor.GRAY + "[" + PrisonGame.api.getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrefix() + ChatColor.GRAY + "] " + player.getDisplayName());
                        player.setPlayerListName(ChatColor.GRAY + "[" + PrisonGame.api.getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrefix() + ChatColor.GRAY + "] " + player.getDisplayName());
                        player.setDisplayName(ChatColor.GRAY + "[" + PrisonGame.api.getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrefix() + ChatColor.GRAY + "] " + player.getDisplayName());
                        }
                    }
                }
            } else {
                player.setPlayerListName(ChatColor.GRAY + "[" + ChatColor.RED + "HARD MODE" + ChatColor.GRAY + "] " + ChatColor.GRAY + player.getName());
            }
    }
}
