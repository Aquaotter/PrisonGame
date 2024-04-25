package prisongame.prisongame.commands.warden;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.MyTask;
import prisongame.prisongame.features.Schedule;

public class SkipCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        var player = (Player) commandSender;

        if (!Schedule.bossBar.getTitle().contains("ROLL CALL")) {
            player.sendMessage(ChatColor.RED + "It is not roll call.");
            return true;
        }

        var world = Bukkit.getWorld("world");

        if (world.getTime() > 0 && world.getTime() < 2000)
            world.setTime(2000);
        else if (world.getTime() > 13000 && world.getTime() < 15000)
            world.setTime(15000);

        Bukkit.broadcastMessage(ChatColor.GREEN + "\nThe warden skipped roll call!\n");

        return true;
    }
}
