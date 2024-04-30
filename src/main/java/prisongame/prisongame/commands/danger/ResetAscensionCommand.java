package prisongame.prisongame.commands.danger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import prisongame.prisongame.keys.Keys;

public class ResetAscensionCommand implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Keys.RANDOM_ITEMS.remove(p);
            Keys.TAX_EVASION.remove(p);
            Keys.SPAWN_PROTECTION.remove(p);
            Keys.REINFORCEMENT.remove(p);
            Keys.SEMICLOAK.remove(p);
            Keys.ASCENSION_COINS.remove(p);
            p.sendMessage(ChatColor.RED + "Your ascension has been reset!");
        }
        return true;
    }
}