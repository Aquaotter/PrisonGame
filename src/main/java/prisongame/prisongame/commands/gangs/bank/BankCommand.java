package prisongame.prisongame.commands.gangs.bank;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.commands.gangs.HelpCommand;
import prisongame.prisongame.commands.gangs.IGangCommand;
import prisongame.prisongame.gangs.GangRole;
import prisongame.prisongame.keys.Keys;

import java.util.Arrays;

public class BankCommand implements IGangCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0)
            return new DefaultCommand().onCommand(sender, command, label, args);

        var player = (Player) sender;
        var subcommand = args[0];
        var rest = Arrays.stream(args).toList().subList(1, args.length).toArray(String[]::new);

        var executor = switch (subcommand) {
            case "deposit" -> new DepositCommand();
            case "withdraw" -> new WithdrawCommand();
            case "approve" -> new ApproveCommand();
            case "deny" -> new DenyCommand();
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

    @Override
    public GangRole getRole() {
        return GangRole.MEMBER;
    }
}
