package prisongame.prisongame.commands.gangs.bank;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.commands.gangs.IGangCommand;
import prisongame.prisongame.gangs.GangRole;
import prisongame.prisongame.gangs.Gangs;
import prisongame.prisongame.keys.Keys;

import java.sql.SQLException;

public class DepositCommand implements IGangCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        var player = (Player) sender;

        if (args.length < 1 || !args[0].matches("^\\d+(\\.\\d+)?$")) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Please provide an amount."));
            return true;
        }

        var amount = Double.parseDouble(args[0]);

        if (amount > Keys.MONEY.get(player, 0.0)) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>You don't have enough money."));
            return true;
        }

        if (amount < 1) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Please provide a larger amount."));
            return true;
        }

        try {
            var gang = Gangs.get(player);

            gang.bank += amount;
            Keys.MONEY.set(player, Keys.MONEY.get(player, 0.0) - amount);
            Keys.GANG_CONTRIBUTION.set(player, Keys.GANG_CONTRIBUTION.get(player, 0.0) + amount);
            Gangs.update(gang);

            for (var member : Bukkit.getOnlinePlayers()) {
                var memberGang = Keys.GANG.get(member);

                if (memberGang == null || !memberGang.equalsIgnoreCase(Keys.GANG.get(player)))
                    continue;

                member.sendMessage(PrisonGame.mm.deserialize(
                        "<gray><player> has deposited <amount> (now <total>)",
                        Placeholder.component("player", Component
                                .text(player.getName())
                                .color(NamedTextColor.WHITE)),
                        Placeholder.component("amount", Component
                                .text(PrisonGame.formatBalance(amount) + "$")
                                .color(NamedTextColor.GREEN)),
                        Placeholder.component("total", Component
                                .text(PrisonGame.formatBalance(gang.bank) + "$")
                                .color(NamedTextColor.GREEN))
                ));
            }
        } catch (SQLException exception) {
            player.sendMessage(PrisonGame.mm.deserialize("<red>An error occurred."));
            PrisonGame.instance.getLogger().severe(exception.getMessage());
        }

        return true;
    }

    @Override
    public GangRole getRole() {
        return GangRole.MEMBER;
    }
}
