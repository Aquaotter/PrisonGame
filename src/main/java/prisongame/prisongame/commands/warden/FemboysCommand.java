package prisongame.prisongame.commands.warden;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;

public class FemboysCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        PrisonGame.FEMBOYS = !PrisonGame.FEMBOYS;
        if (PrisonGame.FEMBOYS)
            Bukkit.broadcastMessage(ChatColor.RED + "Warden has enabled FEMBOYS mode!" + ChatColor.GRAY + " (Nurses are FEMBOYS)");
        if (!PrisonGame.FEMBOYS)
            Bukkit.broadcastMessage(ChatColor.RED + "Warden has disabled FEMBOYS mode!" + ChatColor.GRAY + " (Nurses are no longer FEMBOYS (NOOOOOOOOOOOOOOo))");
        return true;
    }
}