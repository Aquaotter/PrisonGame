package prisongame.prisongame.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.commands.gangs.*;
import prisongame.prisongame.commands.gangs.AcceptCommand;
import prisongame.prisongame.commands.gangs.ResignCommand;
import prisongame.prisongame.commands.gangs.bank.BankCommand;
import prisongame.prisongame.gangs.GangRole;
import prisongame.prisongame.keys.Keys;

import java.util.Arrays;

public class GangsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>You aren't a player."));
            return true;
        }

        if (args.length == 0)
            return showHelp(player, command);

        var subcommand = args[0];
        var rest = Arrays.stream(args).toList().subList(1, args.length).toArray(String[]::new);

        var executor = switch (subcommand) {
            case "leaderboard" -> new LeaderboardCommand();
            case "official" -> new OfficialCommand();
            case "disband" -> new DisbandCommand();
            case "create" -> new CreateCommand();
            case "invite" -> new InviteCommand();
            case "accept" -> new AcceptCommand();
            case "resign" -> new ResignCommand();
            case "leave" -> new LeaveCommand();
            case "list" -> new ListCommand();
            case "bank" -> new BankCommand();
            case "join" -> new JoinCommand();
            case "fire" -> new FireCommand();
            case "kick" -> new KickCommand();
            case "info" -> new InfoCommand();
            case "view" -> new ViewCommand();
            default -> new HelpCommand();
        };

        if (!GangRole.values()[Keys.GANG_ROLE.get(player, 0)].isAtLeast(executor.getRole())) {
            player.sendMessage(PrisonGame.mm.deserialize(
                    "<red>You need to be a gang <role> to do that.",
                    Placeholder.component("role", Component.text(executor.getRole().name().toLowerCase()))
            ));
            return true;
        }

        return executor.onCommand(player, command, subcommand, rest);
    }

    private boolean showHelp(Player player, Command command) {
        new HelpCommand().onCommand(player, command, "help", new String[] {});
        return true;
    }
}
