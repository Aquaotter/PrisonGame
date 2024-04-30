package prisongame.prisongame.commands.completers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.gangs.Gangs;

import java.sql.SQLException;
import java.util.List;

public class GangsCompleter extends SubcommandCompleter {
    public GangsCompleter() {
        super("gangs", new String[] {
                "leaderboard",
                "official",
                "disband",
                "create",
                "invite",
                "resign",
                "leave",
                "list",
                "bank",
                "info",
                "fire",
                "kick",
                "join",
                "help",
                "view"
        });
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase(name))
            return null;

        return switch (args.length) {
            case 1 -> super.onTabComplete(sender, command, label, args);
            case 2 -> {
                var subcommand = args[0].toLowerCase();

                yield switch (subcommand) {
                    case "invite", "official", "fire", "kick" -> complete(args[1], getPlayers());
                    case "join" -> complete(args[1], getGangs());
                    case "bank" -> complete(args[1], new String[] {"deposit", "withdraw", "approve", "deny"});
                    default -> null;
                };
            }
            default -> null;
        };
    }

    private String[] getGangs() {
        try {
            return Gangs.list()
                    .stream()
                    .map((g) -> g.name)
                    .toArray(String[]::new);
        } catch (SQLException exception) {
            return new String[0];
        }
    }

    private String[] getPlayers() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).toArray(String[]::new);
    }
}
