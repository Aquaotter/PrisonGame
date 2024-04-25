package prisongame.prisongame.commands.completers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BalanceCompleter extends SubcommandCompleter {
    public BalanceCompleter() {
        super("balance", new String[] {
                "leaderboard",
                "view"
        });
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2 || !args[0].equalsIgnoreCase("view"))
            return super.onTabComplete(sender, command, label, args);

        return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }
}
