package prisongame.prisongame.commands.economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import prisongame.prisongame.keys.Keys;

import java.text.NumberFormat;

public class PayCommand implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Console cannot pay people. If you want to set somebody's money, use /setmoney.");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(ChatColor.RED + "Invalid player provided. (player is not online)");
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage(ChatColor.RED + "Invalid player provided. (cant pay self)");
            return true;
        }

        try {
            double value = Double.valueOf(args[1]);

            if (value < 1) {
                player.sendMessage(ChatColor.RED + "Please provide a valid amount. (amount is less than one)");
                return true;
            }

            if (!Keys.MONEY.has(player)) {
                player.sendMessage(ChatColor.RED + "You don't have enough money. (no pdc error)");
                return true;
            }

            double playerAmount = Keys.MONEY.get(player);
            double targetAmount = Keys.MONEY.get(target, 0.0);

            if (playerAmount < value) {
                player.sendMessage(ChatColor.RED + "You don't have enough money.");
                return true;
            }

            playerAmount -= value;
            targetAmount += value;
            Keys.MONEY.set(player, playerAmount);
            Keys.MONEY.set(target, targetAmount);

            player.sendMessage(String.format("%sPaid $%s to %s.", ChatColor.GREEN, formatDouble(value), target.getName()));
            target.sendMessage(String.format("%s%s paid you $%s.", ChatColor.GREEN, player.getName(), formatDouble(value)));
        } catch (NumberFormatException exception) {
            player.sendMessage(ChatColor.RED + "Please provide a valid amount. (not a number)");
        }

        return true;
    }

    private String formatDouble(double value) {
        NumberFormat format = NumberFormat.getInstance();

        if (value % 1 == 0)
            return format.format((int) value);

        return format.format(value);
    }
}