package prisongame.prisongame.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import oshi.jna.platform.mac.SystemB;
import prisongame.prisongame.config.Prison;

import static prisongame.prisongame.config.ConfigKt.*;
import static prisongame.prisongame.listeners.InventoryClickListener.switchMap;

public class ForceMapCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        String name = strings[0];
        String map = "NULL";
        for (var prison : getConfig().getPrisons().values()) {
            if(name.equals(prison.getName())) {
                switchMap(prison);
                map = prison.getDisplayName();
            }
        }
        Bukkit.broadcastMessage(ChatColor.RED+"\nMAP FORCE TO "+ChatColor.RESET+map+ChatColor.RED+ " !\n");
        return true;
    }
}
