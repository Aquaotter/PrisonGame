package prisongame.prisongame.commands.economy.staff;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import prisongame.prisongame.keys.Keys;

public class NerdCheatCommand implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Bukkit.broadcastMessage("lmao!!! " + sender.getName() + " used the cheat command to give them 1000$!! probbably was just testing but what a pussy L!!!!");
        Player p = (Player) sender;
        Keys.MONEY.set(p, Keys.MONEY.get(p, 0.0) + 1000.0);
        return true;
    }
}
