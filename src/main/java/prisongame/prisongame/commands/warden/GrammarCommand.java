package prisongame.prisongame.commands.warden;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;

public class GrammarCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        PrisonGame.grammar = !PrisonGame.grammar;
        if (PrisonGame.grammar)
            Bukkit.broadcastMessage(ChatColor.RED + "Warden has enabled grammar mode!" + ChatColor.GRAY + " (Guards will now speak with proper grammar)");
        if (!PrisonGame.grammar)
            Bukkit.broadcastMessage(ChatColor.RED + "Warden has disabled grammar mode!" + ChatColor.GRAY + " (Guards are no longer forced to speak with proper grammar)");
        return true;
    }
}