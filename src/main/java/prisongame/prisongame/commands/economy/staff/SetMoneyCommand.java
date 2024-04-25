package prisongame.prisongame.commands.economy.staff;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.keys.Keys;

public class SetMoneyCommand implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        var player = Bukkit.getPlayer(args[0]);

        if (player == null) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>That player isn't online."));
            return true;
        }

        if (args[1].replace("-", "").equalsIgnoreCase("inf")) {
            var isNegative = args[1].startsWith("-");
            var amount = isNegative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
            Keys.MONEY.set(player, amount);
        } else if (args[1].matches("^\\d+(\\.\\d+)?")) {
            var amount = Double.valueOf(args[1]);
            Keys.MONEY.set(player, amount);
        } else {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Please provide an amount."));
            return true;
        }

        sender.sendMessage(PrisonGame.mm.deserialize(
                "<gray>Set money of <white><player></white> to <white><amount>$</white>.",
                Placeholder.component("player", player.name()),
                Placeholder.component("amount", Component.text(PrisonGame.formatBalance(Keys.MONEY.get(player, 0.0))))
        ));

        return true;
    }
}
