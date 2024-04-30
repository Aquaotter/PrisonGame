package prisongame.prisongame.commands.warden;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.MyListener;
import prisongame.prisongame.PrisonGame;

public class ResignCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (PrisonGame.wardenCooldown <= 0) {
            MyListener.playerJoin(PrisonGame.warden, false);
            Bukkit.broadcastMessage(ChatColor.GREEN + "The warden has resigned!");
            PrisonGame.wardenCooldown = 60;
            PrisonGame.warden = null;
        }

        return true;
    }
}