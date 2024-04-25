package prisongame.prisongame.commands.warden;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;

public class MuteChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        /*PrisonGame.chatmuted = !PrisonGame.chatmuted;
        if (PrisonGame.chatmuted)
            Bukkit.broadcastMessage(ChatColor.RED + "Warden has muted the chat!");
        if (!PrisonGame.chatmuted)
            Bukkit.broadcastMessage(ChatColor.RED + "Warden has unmuted the chat!");
         */
        sender.sendMessage(ChatColor.RED + "Sorry, This Action is now Removed Contact 4950 or _Aquaotter_ is this is a issue!");
        return true;
    }
}