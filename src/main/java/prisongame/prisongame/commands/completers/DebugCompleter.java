package prisongame.prisongame.commands.completers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.commands.staff.debug.PDCCommand;
import prisongame.prisongame.keys.Keys;
import prisongame.prisongame.lib.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DebugCompleter extends SubcommandCompleter {
    public DebugCompleter() {
        super("debug", new String[] {
                "pdc",
                "force",
                "protection",
                "timer"
        });
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase(name))
            return null;

        var allPlayers = Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();

        return switch (args.length) {
            case 1 -> super.onTabComplete(sender, command, label, args);
            case 2 -> {
                var subcommand = args[0];

                yield switch (subcommand) {
                    case "pdc" -> complete(args[1], Arrays.stream(PDCCommand.PDCAction.values()).map((a) -> a.name().toLowerCase()).toArray(String[]::new));
                    case "force" -> complete(args[1], allPlayers.toArray(String[]::new));
                    default -> null;
                };
            }
            case 3 -> {
                var subcommand = args[0];

                yield switch (subcommand) {
                    case "pdc" -> complete(args[2], allPlayers.toArray(String[]::new));
                    case "force" -> Arrays.stream(Role.values()).map((r) -> r.name().toLowerCase()).toList();
                    default -> null;
                };
            }
            case 4 -> {
                var subcommand = args[0];

                yield switch (subcommand) {
                    case "pdc" -> complete(args[3], Arrays.stream(Keys.values()).map((k) -> k.name().toLowerCase()).toArray(String[]::new));
                    case "force" -> complete(args[3], new String[] {"-a", "--action"});
                    default -> null;
                };
            }
            default -> null;
        };
    }
}
