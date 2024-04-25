package prisongame.prisongame.commands.staff;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.commands.staff.debug.*;

import java.util.Arrays;

public class DebugCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0)
            return showHelp(sender, command);

        var rest = Arrays.stream(args).toList().subList(1, args.length).toArray(new String[0]);
        var subcommand = switch (args[0]) {
            case "pdc" -> new PDCCommand();
            case "force" -> new ForceCommand();
            case "protection" -> new ProtectionCommand();
            case "timer" -> new TimerCommand();
            default -> new HelpCommand();
        };
        subcommand.onCommand(sender, command, args[0], rest);

        return true;
    }

    public static boolean showHelp(CommandSender sender, Command command) {
        new HelpCommand().onCommand(sender, command, "help", new String[] {});
        return true;
    }
}
