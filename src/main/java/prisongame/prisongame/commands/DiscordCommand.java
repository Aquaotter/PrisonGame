package prisongame.prisongame.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static prisongame.prisongame.config.ConfigKt.getConfig;

public class DiscordCommand implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.BLUE + "https://discord.gg/" + getConfig().getGeneral().getDiscordInvite());
        return true;
    }
}
