package prisongame.prisongame.commands.warden;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.MyListener;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.lib.Role;

public class ReleaseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) return true;

        Player p = Bukkit.getPlayer(args[0]);

        if (p == null || !p.getDisplayName().contains("SOLITARY")) return true;

        PrisonGame.tptoBed(p);
        MyListener.playerJoin(p, false);
        Bukkit.broadcastMessage(ChatColor.GRAY + p.getName() + " was released from solitary!");

        return true;
    }
}
