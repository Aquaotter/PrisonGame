package prisongame.prisongame.commands.economy.staff;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import prisongame.prisongame.keys.Keys;

public class ResetMoneyCommand implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Keys.MONEY.set(Bukkit.getPlayer(args[0]), 0.0);
        return true;
    }
}